package com.notyy.visualfp.example1

import scala.io.Source

object ReadModel {
  private val ElementPattern = """class (.*) <<(.*)>>""".r
  private val ConnectionPattern = """(.*) -> (.*)""".r

  def execute(modelName: String): Model = {
    val source = Source.fromFile(s"./localOutput/$modelName.puml")
    val model = source.getLines().toVector.filterNot(_.startsWith("@")).filterNot(_.isEmpty).
      foldLeft(Model(modelName,Vector.empty, Vector.empty)){
        case (model, line) => line match {
          case (ElementPattern(elementName, elementType)) => model.copy(elements = model.elements.appended(Element(elementType,elementName)))
          case (ConnectionPattern(from, to)) => model.copy(connections = model.connections.appended(Connection(from, to)))
          case _@s => {println(s"what's this '$s'"); model}
        }
      }
    source.close()
    model
  }
}