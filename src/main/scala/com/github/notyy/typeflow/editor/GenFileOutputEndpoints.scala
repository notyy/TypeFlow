package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{FileOutputEndpoint, OutputEndpoint}

class GenFileOutputEndpoints(private val genFileOutputEndpoint: GenFileOutputEndpoint) {
  def execute(codeLang: CodeLang, fileOutputEndpoints: Vector[FileOutputEndpoint], packageName: PackageName, codeTemplate: CodeTemplate): Vector[JavaCode] = {
    codeLang match {
      case JAVA_LANG => fileOutputEndpoints.map(oe => genFileOutputEndpoint.execute(packageName, oe, codeTemplate))
      case SCALA_LANG => ???
    }
  }
}
