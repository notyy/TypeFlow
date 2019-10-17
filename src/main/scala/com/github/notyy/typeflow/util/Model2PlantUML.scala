package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.Model
import com.github.notyy.typeflow.editor.PlantUML

object Model2PlantUML {
  //shows the plantuml diagram of active flow
  def execute(model: Model): PlantUML = {
    val definitions = model.activeFlow.get.instances.map(_.definition)
    val defBlock = definitions.map(defi => s"class ${defi.name} <<${ModelUtil.definitionType(defi)}>>").mkString(System.lineSeparator)
    val connectionBlock = model.activeFlow.get.connections.map(conn => {
      val outputType = ModelUtil.findOutputType(conn.fromInstanceId, conn.outputIndex, model)
      s"${conn.fromInstanceId} --> $outputType${System.lineSeparator}" +
        s"$outputType --> ${conn.toInstanceId}"
    }).mkString(System.lineSeparator).linesIterator.distinct.mkString(System.lineSeparator) //to avoid duplicated connections from same instance to it's output
    val rs = s"""
       |@startuml
       |$defBlock
       |
       |$connectionBlock
       |@enduml
       |""".stripMargin
    PlantUML(rs)
  }
}
