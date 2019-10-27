package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, CommandLineInputEndpoint, Definition, Input, InputEndpoint, Model, Output, OutputEndpoint, PureFunction}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

sealed trait CodeLang
case object LANG_SCALA extends CodeLang
case object LANG_JAVA extends CodeLang

object CodeLang {
  def from(lang: String): CodeLang = lang match {
    case "scala" => LANG_SCALA
    case "java" => LANG_JAVA
  }
}

object Model2Code {
  val PLATFORM_LOCAL = "local"
  val PLATFORM_ALIYUN = "aliyun"


  private val logger = Logger(Model2Code.getClass)
  type CodeFileName = String
  type CodeContent = String

  def execute(model: Model, packageName: String, platform: String, lang: CodeLang): Map[CodeFileName, CodeContent] = {
    val definitions: Vector[Definition] = model.definitions
    val localCodes: Vector[(CodeFileName, CodeContent)] = definitions.flatMap { defi =>
      val codeFileName = s"${defi.name}.${if (lang == LANG_SCALA) "scala" else "java"}"
      val codeContent: Option[CodeContent] = defi match {
        case CommandLineInputEndpoint(name, outputType) => {
          Some(ReadFile.execute(Path("./code_template/scala/CommandLineInputEndpoint.scala")).get.
            replaceAllLiterally("$InputEndpointName$", defi.name).
            replaceAllLiterally("$packageName$", packageName))
        }
        case PureFunction(name, inputs, outputs) => Some(genLocalCode(packageName, defi, lang))
        case OutputEndpoint(_,_,_,_) => Some(genLocalCode(packageName, defi, lang))
        case _ => None
      }
      codeContent.map(cc => (codeFileName, cc))
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
                replaceAllLiterally("$writeOutput$", genWriteOutput(defi.outputs)).
                replaceAllLiterally("$Callee$", if(lang == LANG_SCALA) defi.name else s"new ${defi.name}()")
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
    if (outputs.isEmpty || outputs.head.outputType.name == "Unit") "" else "output.write(JSONUtil.toJSON(Param(value)).getBytes)"
  }

  private def genLocalCode(packageName: String, defi: Definition, lang: CodeLang): String = {
    if (lang == LANG_SCALA) {
      s"""|package $packageName
          |
          |object ${defi.name} {
          |  def execute(${genFormalParams(defi.inputs,lang)}): ${genReturnType(defi.outputs)} = {
          |    ???
          |  }
          |}
          |""".stripMargin
    } else {
      s"""|package $packageName;
          |
          |public class ${defi.name} {
          |  public ${genReturnType(defi.outputs)} execute(${genFormalParams(defi.inputs,lang)}) {
          |    return null;
          |  }
          |}
          |""".stripMargin
    }
  }

  def genFormalParams(inputs: Vector[Input], lang: CodeLang): String = {
    if (inputs.isEmpty) {
      ""
    } else {
      inputs.map { input =>
        val inputTypeName = input.inputType.name
        if (inputTypeName == "Unit") ""
        else {
          val inputParamTypeName = if (inputTypeName == "Unit") "" else inputTypeName
          val paramName = s"param${input.index}"
          lang match {
            case LANG_SCALA =>
              s"$paramName: $inputParamTypeName"
            case LANG_JAVA => s"$inputParamTypeName $paramName"
          }
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
      s"(${
        inputs.map {
          _.inputType.name
        }.reduce((x1, x2) => s"$x1,$x2")
      })"
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
