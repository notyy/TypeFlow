package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, PureFunction}

class GenJavaPureFunction {
  def execute(packageName: PackageName, pureFunction: PureFunction, codeTemplate: CodeTemplate): JavaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", pureFunction.name).
      replaceAllLiterally("$ReturnType$", pureFunction.outputs.head.outputType.name).
      replaceAllLiterally("$Params$", genFormalParams(pureFunction.inputs))
    JavaCode(s"${packageName.value}.${pureFunction.name}",code)
  }

  def genFormalParams(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else {
      inputs.map { input =>
        val inputTypeName = input.inputType.name
        if (inputTypeName == "Unit") ""
        else {
          val inputParamTypeName = if (inputTypeName == "Unit") "" else inputTypeName
          val paramName = s"param${input.index}"
          s"$inputParamTypeName $paramName"
        }
      }.reduce((x1, x2) => s"$x1,$x2")
    }
  }
}
