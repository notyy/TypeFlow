package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Definition, Input}

class GenAliyunHandler(val genJSonParamType: GenJSonParamType, val genWriteOut: GenWriteOut) {
  def execute(packageName: PackageName, definition: Definition, codeTemplate: CodeTemplate): ScalaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", definition.name).
      replaceAllLiterally("$Params$", genJSonParamType.execute(definition.inputs)).
      replaceAllLiterally("$Callee$", s"new ${definition.name}()").
      replaceAllLiterally("$WriteOutput$", genWriteOut.execute(definition.outputs)).
      replaceAllLiterally("$ParamCall$", genParamCall(definition.inputs))
    ScalaCode(QualifiedName(s"${packageName.value}.aliyun.${definition.name}Handler"), code)
  }



  private def genParamCall(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else if (inputs.size == 1) {
      val inputName = inputs.head.inputType.name
      if (inputName == "Unit") "" else "param.value"
    } else {
      inputs.map(input => s"param.value._${input.index}").reduce((x1, x2) => s"$x1,$x2")
    }
  }
}
