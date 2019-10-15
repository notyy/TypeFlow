package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.{Definition, Function, InputEndpoint, Model, OutputEndpoint, OutputType}

object ModelUtil {
  def definitionType(defi: Definition): String = defi.getClass.getSimpleName

  def findOutputType(instanceId: String, outputIndex: Int, model: Model): String = {
    (model.activeFlow.instances.find(_.id == instanceId).get.definition match {
      case InputEndpoint(name, outputType) => outputType
      case Function(name, input, outputs) => outputs.find(_.index == outputIndex).get.outputType
      case OutputEndpoint(name, inputType, outputType, errorOutput) => outputType
    }).name
  }
}
