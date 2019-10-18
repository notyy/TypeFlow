package com.github.notyy.typeflow.editor

object TestReadUML extends App {
  val plantUML = PlantUML(ReadFile.execute("example/example.puml"))
  PlantUML2Model.execute(plantUML)
}
