package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger

class GenCommandLineInputEndpoint(val genCallingChain: GenCallingChain) {
  private val logger = Logger(this.getClass)

  def execute(packageName: PackageName, commandLineInputEndpoint: CommandLineInputEndpoint, codeTemplate: CodeTemplate, model: Model): ScalaCode = {
    val flow: Flow = model.activeFlow.get
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", commandLineInputEndpoint.name).
      replaceAllLiterally("$CallingChain$", genCallingChain.execute(flow.instances.find(_.id == commandLineInputEndpoint.name).get, 1, "input", flow.connections, flow.instances, Vector.empty).mkString(System.lineSeparator()))
    ScalaCode(QualifiedName(s"${packageName.value}.${commandLineInputEndpoint.name}"), code)
  }
}
