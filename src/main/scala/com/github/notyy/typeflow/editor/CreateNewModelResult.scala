package com.github.notyy.typeflow.editor

trait CreateNewModelResult
case class ModelCreationSuccess(modelName: String) extends CreateNewModelResult
case class ModelCreateFailed(modelName: String, msg: String) extends CreateNewModelResult
