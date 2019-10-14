package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.util.JSONUtil

object Model2Json {
  def execute(model: Model): String = JSONUtil.toJSON(model)
}
