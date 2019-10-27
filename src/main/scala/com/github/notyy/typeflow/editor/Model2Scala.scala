package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, CommandLineInputEndpoint, Definition, Input, InputEndpoint, Model, Output}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

object Model2Scala {
  val PLATFORM_LOCAL = "local"
  val PLATFORM_ALIYUN = "aliyun"

  private val logger = Logger(Model2Scala.getClass)
  type CodeFileName = String
  type CodeContent = String

  def execute(model: Model, packageName: String, platform: String): Map[CodeFileName, CodeContent] = {
    val definitions: Vector[Definition] = model.definitions
    val localCodes: Vector[(CodeFileName, CodeContent)] = definitions.map { defi =>
      val codeFileName = s"${defi.name}.scala"
      val codeContent: CodeContent = defi match {
        case CommandLineInputEndpoint(name, outputType) => {
          ReadFile.execute(Path("./code_template/scala/CommandLineInputEndpoint.scala")).get.
            replaceAllLiterally("$InputEndpointName$", defi.name).
            replaceAllLiterally("$packageName$", packageName)
        }
        case _ => genLocalCode(packageName, defi)
      }
      (codeFileName, codeContent)
    }

    val platformCodes: Vector[(CodeFileName, CodeContent)] = {
      if (platform == PLATFORM_ALIYUN) {
        definitions.flatMap { defi =>
          val codeFileName = s"aliyun/${defi.name}Handler.scala"
          val codeContent: Option[CodeContent] = defi match {
            case CommandLineInputEndpoint(name, outputType) => {
              None
            }
            case AliyunHttpInputEndpoint(name, outputType) => {
              Some(ReadFile.execute(Path("./code_template/scala/AliyunHttpInputEndpoint.scala")).get.
                replaceAllLiterally("$InputEndpointName$", defi.name).
                replaceAllLiterally("$packageName$", packageName).
                replaceAllLiterally("$OutputType$", TypeUtil.removeDecorate(outputType.name)).
                //TODO replace this
                replaceAllLiterally("$bucketName$", "type-flow").
                replaceAllLiterally("$objectName$", s"${model.name}"))
            }
            case _ if platform == PLATFORM_ALIYUN => {
              Some(ReadFile.execute(Path("./code_template/scala/StreamRequestHandlerTemplate.scala")).get.
                replaceAllLiterally("$TypeFlowFunction$", defi.name).
                replaceAllLiterally("$params$", genActualParams(defi.inputs)).
                replaceAllLiterally("$packageName$", packageName).
                replaceAllLiterally("$paramCall$", genParamCall(defi.inputs)).
                replaceAllLiterally("$writeOutput$",genWriteOutput(defi.outputs))
              )
            }
          }
          codeContent.map(cc => (codeFileName, cc))
        }
      } else Vector.empty
    }

    (localCodes ++ platformCodes).toMap
  }

  def genWriteOutput(outputs: Vector[Output]): String = {
    if(outputs.isEmpty || outputs.head.outputType.name == "Unit") "" else "output.write(JSONUtil.toJSON(Param(value)).getBytes)"
  }

  private def genLocalCode(packageName: String, defi: Definition): String = {
    s"""|package $packageName
        |
        |object ${defi.name} {
        |  def execute(${genFormalParams(defi.inputs)}): ${genReturnType(defi.outputs)} = {
        |    ???
        |  }
        |}
        |""".stripMargin
  }

  def genFormalParams(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else {
      inputs.map { input =>
        val inputTypeName = input.inputType.name
        if (inputTypeName == "Unit") ""
        else {
          s"param${input.index}: ${if (inputTypeName == "Unit") "" else inputTypeName}"
        }
      }.reduce((x1, x2) => s"$x1,$x2")
    }
  }

  def genActualParams(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else if (inputs.size == 1) {
      val inputName = inputs.head.inputType.name
      if (inputName == "Unit") "" else inputName
    } else {
      s"(${inputs.map{_.inputType.name }.reduce((x1, x2) => s"$x1,$x2")})"
    }
  }

  def genParamCall(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else if (inputs.size == 1) {
      val inputName = inputs.head.inputType.name
      if (inputName == "Unit") "" else "param.value"
    } else {
      inputs.map(input => s"param.value._${input.index}").reduce((x1, x2) => s"$x1,$x2")
    }
  }

  def genReturnType(outputs: Vector[Output]): String = {
    val distinctOutputs = outputs.distinct
    if (distinctOutputs.isEmpty) {
      "Unit"
    } else {
      if (distinctOutputs.size == 1) TypeUtil.removeDecorate(distinctOutputs.head.outputType.name)
      else {
        if (distinctOutputs.forall(o => TypeUtil.PrimitiveTypeNameMap.contains(TypeUtil.removeDecorate(o.outputType.name)))) {
          "Any"
        } else {
          //TODO fix this later
          logger.warn("multiple outputs of different types, should fix this")
          ""
        }
      }
    }
  }
}
