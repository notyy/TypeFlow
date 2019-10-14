package com.github.notyy.typeflow.editor

import UserInputInterpreter.CreateModelCommand

object CreateModel {
  case class UnsavedModel(modelName: String)
  def execute(command: CreateModelCommand): UnsavedModel = {
    UnsavedModel(command.modelName)
  }
}
