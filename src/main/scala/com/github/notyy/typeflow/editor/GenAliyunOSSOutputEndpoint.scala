package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.AliyunOSSOutputEndpoint

class GenAliyunOSSOutputEndpoint(val genFormalParams: GenFormalParams, val genWriteOut: GenWriteOut) {
  def execute(packageName: PackageName, aliyunOSSOutEndpoint: AliyunOSSOutputEndpoint, codeTemplate: CodeTemplate): ScalaCode = {
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", aliyunOSSOutEndpoint.name).
      replaceAllLiterally("$ReturnType$", replaceEmptyReturnTypeWithVoid(aliyunOSSOutEndpoint.outputs.head.outputType.name)).
      replaceAllLiterally("$WriteOutput$", genWriteOut.execute(aliyunOSSOutEndpoint.outputs)).
      replaceAllLiterally("$Params$", genFormalParams.execute(aliyunOSSOutEndpoint.inputs))
    ScalaCode(QualifiedName(s"${packageName.value}.aliyun.${aliyunOSSOutEndpoint.name}Handler"), code)
  }

  def replaceEmptyReturnTypeWithVoid(returnType: String): String = {
    if (returnType == "Unit") "void" else returnType
  }
}
