package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger

class GenFileInputEndpoint(val genCallingChain: GenCallingChain) {
  private val logger = Logger(this.getClass)

  def execute(packageName: PackageName, fileInputEndpoint: FileInputEndpoint, codeTemplate: CodeTemplate, model: Model): ScalaCode = {
    val flow: Flow = model.activeFlow.get
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", fileInputEndpoint.name).
      replaceAllLiterally("$CallingChain$", genCallingChain.execute(flow.instances.find(_.id == fileInputEndpoint.name).get, 1, "input", flow.connections, flow.instances).mkString(System.lineSeparator()))
    ScalaCode(QualifiedName(s"${packageName.value}.${fileInputEndpoint.name}"), code)
  }
}
