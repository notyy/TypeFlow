package com.github.notyy.typeflow.editor

import java.io.{File, PrintWriter}

import CreateNewModel.{ModelCreationSuccess, ModelSaveFailed}
import com.github.notyy.typeflow.domain.Model

import scala.util.{Failure, Success, Try}

object UpdateModel {
  trait Result
  case class ModelUpdateSuccess(modelName: String) extends Result
  case class ModelUpdateFailed(modelName: String, msg: String) extends Result
  def execute(modifiedModel: Model): Result = {
//    Try {
//      val elements = modifiedModel.elements.map(element => s"class ${element.name} <<${element.elementType}>>")
//      val connections = modifiedModel.connections.map(connection => s"${connection.from} -> ${connection.to}")
//      val modelStr =
//        s"""@startuml
//          |${if(elements.isEmpty) "" else elements.reduce(_+"\n"+_)}
//          |${if(connections.isEmpty) "" else connections.reduce(_+"\n"+_)}
//          |@enduml
//          |""".stripMargin
//      val file = new File(s"./localOutput/${modifiedModel.name}.puml")
//      val writer = new PrintWriter(file)
//      writer.println(modelStr)
//      writer.flush()
//      ModelUpdateSuccess(modifiedModel.name)
//    } match {
//      case Success(rs) => rs
//      case Failure(exception) => {
//        exception.printStackTrace()
//        ModelUpdateFailed(modifiedModel.name, exception.getMessage)
//      }
//    }
      ???
  }
}
