package com.github.notyy.typeflow.editor

object GetModelPath {
  def execute(modelName: String): Path = OutputPath(s"./localoutput/${modelName}.typeflow")
}
