package com.github.notyy.typeflow.editor

object Command2ModelName {
  def execute(command: AddDefinitionCommand): String = command.modelName
}
