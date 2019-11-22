package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{FileInputEndpoint, Model}

class GenFileInputEndpoints(private val genFileInputEndpoint: GenFileInputEndpoint) {
  def execute(fileInputEndpoints: Vector[FileInputEndpoint], packageName: PackageName, codeTemplate: CodeTemplate, model: Model): Vector[ScalaCode] = {
    fileInputEndpoints.map(fie => genFileInputEndpoint.execute(packageName, fie, codeTemplate, model))
  }
}
