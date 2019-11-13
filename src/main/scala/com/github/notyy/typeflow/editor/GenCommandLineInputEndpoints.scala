package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.CommandLineInputEndpoint

class GenCommandLineInputEndpoints(val genCommandLineInputEndpoint: GenCommandLineInputEndpoint) {
  def execute(commandLineInputEndpoints: Vector[CommandLineInputEndpoint], packageName: PackageName, codeTemplate: CodeTemplate): Vector[ScalaCode] = {
    commandLineInputEndpoints.map(cie => genCommandLineInputEndpoint.execute(packageName,cie, codeTemplate))
  }
}
