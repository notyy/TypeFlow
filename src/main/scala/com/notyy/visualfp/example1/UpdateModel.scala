package com.notyy.visualfp.example1

import java.io.{File, PrintWriter}

import com.notyy.visualfp.example1.SaveNewModel.{ModelCreationSuccess, ModelSaveFailed}

import scala.util.{Failure, Success, Try}

object UpdateModel {
  trait Result
  case class ModelUpdateSuccess(modelName: String) extends Result
  case class ModelUpdateFailed(modelName: String, msg: String) extends Result
  def execute(modifiedModel: Model): Result = {
    Try {
      val modelStr =
        s"""@startuml
          |${modifiedModel.elements.map(element => s"class ${element.name} <<${element.elementType}>>").reduce(_+"\n"+_)}
          |@enduml
          |""".stripMargin
      val file = new File(s"./localOutput/${modifiedModel.name}.puml")
      val writer = new PrintWriter(file)
      writer.println(modelStr)
      writer.flush()
      ModelUpdateSuccess(modifiedModel.name)
    } match {
      case Success(rs) => rs
      case Failure(exception) => ModelUpdateFailed(modifiedModel.name, exception.getMessage)
    }
  }
}
