package com.github.notyy.typeflow.editor

object GetModelPath {
  def execute(modelName: String): Path = Path(s"./localoutput/${modelName}.typeflow")
}
