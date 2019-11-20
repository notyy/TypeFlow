package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Definition, Flow, Input, Model, Output}

class GenAliyunHttpInputEndpointHandler(val genJSonParamType4InputEndpoint: GenJSonParamType4InputEndpoint, val genCallingChain: GenCallingChain) {
  def execute(packageName: PackageName, definition: Definition, codeTemplate: CodeTemplate, model: Model): ScalaCode = {
    val flow: Flow = model.activeFlow.get
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", definition.name).
      replaceAllLiterally("$Params$", genJSonParamType4InputEndpoint.execute(definition.outputs)).
      replaceAllLiterally("$CallingChain$", genCallingChain.execute(flow.instances.find(_.id == definition.name).get, 1, "input", flow.connections, flow.instances, Vector.empty).mkString(System.lineSeparator()))
    ScalaCode(QualifiedName(s"${packageName.value}.aliyun.${definition.name}Handler"), code)
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
