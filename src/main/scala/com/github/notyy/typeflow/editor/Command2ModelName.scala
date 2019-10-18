package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model

object Command2ModelName {
  def execute(model: Model): String = model.name
}
