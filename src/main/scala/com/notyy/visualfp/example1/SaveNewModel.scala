package com.notyy.visualfp.example1

import java.io.{File, PrintWriter}

import com.notyy.visualfp.example1.CommandRecorder.logFile
import com.notyy.visualfp.example1.CreateModel.UnsavedModel

import scala.util.{Failure, Success, Try}

object SaveNewModel {

  trait SaveNewModelResult
  case class ModelCreationSuccess(modelName: String) extends SaveNewModelResult
  case class ModelSaveFailed(modelName: String, msg: String) extends SaveNewModelResult

  def execute(model: UnsavedModel): SaveNewModelResult = {
    Try {
      val file = new File(s"./localOutput/${model.modelName}.puml")
      if (file.exists() && file.isFile) {
        file.delete()
      }
      file.createNewFile()
      val modelStr =
        """@startuml
          |@enduml
          |""".stripMargin
      val writer = new PrintWriter(file)
      writer.println(modelStr)
      writer.flush()
      ModelCreationSuccess(model.modelName)
    } match {
      case Success(rs) => rs
      case Failure(exception) => ModelSaveFailed(model.modelName, exception.getMessage)
    }
  }
}
