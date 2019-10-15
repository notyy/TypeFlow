package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.editor.CreateNewModel.{ModelCreationSuccess, ModelSaveFailed}
import com.github.notyy.typeflow.editor.UpdateModel.{ModelUpdateFailed, ModelUpdateSuccess}

object WrapOutput {
  def execute(output: java.lang.Object): WrappedOutput = {
    output match {
      case QuitCommand => WrappedOutput("quit")
      case UnknownCommand(str) => WrappedOutput(s"unknown command '$str'")
      case ModelCreationSuccess(modelName) => WrappedOutput(s"model $modelName created successfully")
      case ModelSaveFailed(modelName,msg) => WrappedOutput(s"model $modelName save failed, error is: $msg")
      case ModelUpdateSuccess(modelName) => WrappedOutput(s"model $modelName updated successfully")
      case ModelUpdateFailed(modelName,msg) => WrappedOutput(s"model $modelName update failed, error is: $msg")
    }
  }
}
