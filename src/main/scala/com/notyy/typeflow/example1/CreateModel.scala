package com.notyy.typeflow.example1

import com.notyy.typeflow.example1.UserInputInterpreter.CreateModelCommand

object CreateModel {
  case class UnsavedModel(modelName: String)
  def execute(command: CreateModelCommand): UnsavedModel = {
    UnsavedModel(command.modelName)
  }
}
