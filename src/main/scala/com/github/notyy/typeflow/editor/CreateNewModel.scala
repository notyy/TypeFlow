package com.github.notyy.typeflow.editor

import java.io.File

import com.github.notyy.typeflow.domain.Model

import scala.util.{Failure, Success, Try}

object CreateNewModel {
  def execute(createModelCommand: CreateModelCommand): Try[CreateNewModelResult] = Try{
    val modelName = createModelCommand.modelName
    val path = s"./localOutput/$modelName.typeflow"
    val file = new File(path)
      if (file.exists() && file.isFile) {
        file.delete()
      }
      file.createNewFile()
      val model = Model(modelName,Vector.empty,Vector.empty,None)
      val json = Model2Json.execute(model)
      SaveToFile.execute(OutputPath(path), json)
      ModelCreationSuccess(modelName)
    }
}
