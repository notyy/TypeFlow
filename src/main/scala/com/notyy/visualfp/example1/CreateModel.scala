package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.UserInputIntepreter.CreateModelCommand

object CreateModel {
  case class UnsavedModel(modelName: String)
  def execute(command: CreateModelCommand): UnsavedModel = {
    UnsavedModel(command.modelName)
  }
}
