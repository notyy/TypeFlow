package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{CommandLineInputEndpoint, Model}

class GenCommandLineInputEndpoints(private val genCommandLineInputEndpoint: GenCommandLineInputEndpoint) {
  def execute(commandLineInputEndpoints: Vector[CommandLineInputEndpoint], packageName: PackageName, codeTemplate: CodeTemplate, model: Model): Vector[ScalaCode] = {
    commandLineInputEndpoints.map(cie => genCommandLineInputEndpoint.execute(packageName, cie, codeTemplate, model))
  }
}
