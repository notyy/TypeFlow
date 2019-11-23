package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.FileOutputEndpoint

class GenFileOutputEndpoint(val genFormalParams: GenFormalParams) {
  def execute(packageName: PackageName, fileOutEndpoint: FileOutputEndpoint, codeTemplate: CodeTemplate): JavaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", fileOutEndpoint.name).
      replaceAllLiterally("$ReturnType$", replaceEmptyReturnTypeWithVoid(fileOutEndpoint.outputs.head.outputType.name)).
      replaceAllLiterally("$Params$", genFormalParams.execute(fileOutEndpoint.inputs))
    JavaCode(QualifiedName(s"${packageName.value}.${fileOutEndpoint.name}"), code)
  }

  def replaceEmptyReturnTypeWithVoid(returnType: String): String = {
    if (returnType == "Unit") "void" else returnType
  }
}
