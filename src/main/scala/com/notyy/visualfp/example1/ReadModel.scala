package com.notyy.visualfp.example1

import scala.io.Source

object ReadModel {
  private val ElementPattern = """class (.*) <<(.*)>>""".r

  def execute(modelName: String): Model = {
    val source = Source.fromFile(s"./localOutput/$modelName.puml")
    val elements = source.getLines().toVector.filterNot(_.startsWith("@")).filterNot(_.isEmpty).map {
      case (ElementPattern(elementName, elementType)) => Element(elementType,elementName)
      case _@s => {println(s"what's this '$s'"); Element("unknown","unknown")}
    }
    source.close()
    Model(modelName,elements,Vector.empty)
  }
}
