package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Definition, Input, Output}

class GenAliyunHandler(val genFormalParams: GenFormalParams) {
  def execute(packageName: PackageName, definition: Definition, codeTemplate: CodeTemplate): ScalaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", definition.name).
      replaceAllLiterally("$Params$", genFormalParams.execute(definition.inputs)).
      replaceAllLiterally("$Callee$", s"new ${definition.name}()").
      replaceAllLiterally("$writeOutput$", genWriteOutput(definition.outputs)).
      replaceAllLiterally("$paramCall$", genParamCall(definition.inputs))
    ScalaCode(QualifiedName(s"${packageName.value}.${definition.name}"), code)
  }

  def genWriteOutput(outputs: Vector[Output]): String = {
    if (outputs.isEmpty || outputs.head.outputType.name == "Unit") "" else "output.write(JSONUtil.toJSON(Param(value)).getBytes)"
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
