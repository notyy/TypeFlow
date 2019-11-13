package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.PureFunction

class GenJavaPureFunction(val genFormalParams: GenFormalParams) {
  def execute(packageName: PackageName, pureFunction: PureFunction, codeTemplate: CodeTemplate): JavaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", pureFunction.name).
      replaceAllLiterally("$ReturnType$", pureFunction.outputs.head.outputType.name.split("::").last).
      replaceAllLiterally("$Params$", genFormalParams.execute(pureFunction.inputs))
    JavaCode(QualifiedName(s"${packageName.value}.${pureFunction.name}"), code)
  }
}
