package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model

object GetModelPath {
  def execute(model: Model): Path = Path(s"./localoutput/${model.name}.typeflow")
}
