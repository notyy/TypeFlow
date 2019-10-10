package com.github.notyy.typeflow.example1

import UserInputInterpreter.CreateModelCommand

object CreateModel {
  case class UnsavedModel(modelName: String)
  def execute(command: CreateModelCommand): UnsavedModel = {
    UnsavedModel(command.modelName)
  }
}
