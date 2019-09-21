package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.SaveNewModel.ModelCreationSuccess
import com.notyy.visualfp.example1.UserInputIntepreter.{QuitCommand, UnknownCommand}

object WrapOutput {
  def execute(output: Object): String = {
    output match {
      case QuitCommand => "quit"
      case UnknownCommand(str) => s"unknown command '$str'"
      case ModelCreationSuccess(modelName) => s"model $modelName created successfully"
    }
  }
}
