package com.github.notyy.typeflow.editor

import java.io.File

import scala.util.{Failure, Success, Try}

object CreateNewModel {
  def execute(createModelCommand: CreateModelCommand): Try[CreateNewModelResult] = Try{
    val modelName = createModelCommand.modelName
      val file = new File(s"./localOutput/$modelName.typeflow")
      if (file.exists() && file.isFile) {
        file.delete()
      }
      file.createNewFile()
      ModelCreationSuccess(modelName)
    }
}
