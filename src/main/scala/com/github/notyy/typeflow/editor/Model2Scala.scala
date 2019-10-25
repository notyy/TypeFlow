package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Definition, Input, InputEndpoint, Model, Output}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

object Model2Scala {
  private val logger = Logger(Model2Scala.getClass)
  type CodeFileName = String
  type CodeContent = String

  def execute(model: Model, packageName: String): Map[CodeFileName, CodeContent] = {
    val definitions: Vector[Definition] = model.definitions
    definitions.map { defi =>
      val codeFileName = s"${defi.name}.scala"
      val codeContent: CodeContent = defi match {
        case InputEndpoint(name, outputType) => {
          ReadFile.execute(Path("./code_template/scala/CommandLineInputEndpoint.scala")).get.replaceAllLiterally("$InputEndpointName$",defi.name)
        }
        case _ => s"""|package $packageName
                      |
                      |object ${defi.name} {
                      |  def execute(${genParams(defi.inputs)}): ${genReturnType(defi.outputs)} = {
                      |    ???
                      |  }
                      |}
                      |""".stripMargin
      }
      (codeFileName, codeContent)
    }.toMap
  }

  def genParams(inputs: Vector[Input]): String = {
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
