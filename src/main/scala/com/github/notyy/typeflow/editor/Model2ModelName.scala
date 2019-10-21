package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model

object Model2ModelName {
  def execute(model: Model): String = model.name
}
