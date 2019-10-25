package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Definition, Model, OutputEndpoint, PureFunction,CommandLineInputEndpoint}

object AddDefinition {
  def execute(savedModel: Model, addDefinitionCommand: AddDefinitionCommand): Model = {
    val newDefinition: Definition = addDefinitionCommand match {
      case AddInputEndpointCommand(modelName, name, outputType) => CommandLineInputEndpoint(name, outputType)
      case AddFunctionCommand(modelName, name, inputs, outputs) => PureFunction(name, inputs, outputs)
      case AddOutputEndpointCommand(modelName, name, inputType, outputType, errorOutput) => OutputEndpoint(name, inputType, outputType, errorOutput)
    }
    savedModel.copy(definitions = savedModel.definitions.appended(newDefinition))
  }
}
