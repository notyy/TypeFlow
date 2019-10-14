package com.github.notyy.typeflow.editor

import java.io.File

import scala.util.{Failure, Success, Try}

object CreateNewModel {

  trait SaveNewModelResult
  case class ModelCreationSuccess(modelName: String) extends SaveNewModelResult
  case class ModelSaveFailed(modelName: String, msg: String) extends SaveNewModelResult

  def execute(modelName: String): SaveNewModelResult = {
    Try {
      val file = new File(s"./localOutput/$modelName.typeflow")
      if (file.exists() && file.isFile) {
        file.delete()
      }
      file.createNewFile()
      ModelCreationSuccess(modelName)
    } match {
      case Success(rs) => rs
      case Failure(exception) => ModelSaveFailed(modelName, exception.getMessage)
    }
  }
}
