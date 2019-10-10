package com.github.notyy.typeflow.example1

import UserInputInterpreter.{AddInputEndpointCommand, ModifyModelCommand}

object AddDefinition {
  def execute(savedModel: Model, addDefinitionCommand: ModifyModelCommand): Model = {
    val newDefinition: Definition = addDefinitionCommand match {
      case AddInputEndpointCommand(modelName,name,outputType) => InputEndpoint(name,outputType)
    }
    savedModel.copy(definitions = savedModel.definitions.appended(newDefinition))
  }
}
