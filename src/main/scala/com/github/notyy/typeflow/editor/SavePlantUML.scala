package com.github.notyy.typeflow.editor

import java.io.{File, PrintWriter}

import scala.util.Try

object SavePlantUML {
  def execute(plantUML: PlantUML): Try[Unit] = Try{
    val file = new File(s"./localoutput/${plantUML.modelName}.puml")
    if (file.exists() && file.isFile) {
      file.delete()
    }
    val writer = new PrintWriter(file)
    writer.print(plantUML.content)
    writer.flush()
    writer.close()
  }
}
