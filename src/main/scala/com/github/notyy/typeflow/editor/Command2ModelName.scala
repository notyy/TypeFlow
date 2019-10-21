package com.github.notyy.typeflow.editor

object Command2ModelName {
  def execute(command: ChangeModelCommand): String = command.modelName
}
