package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}

object Model2Json {
  def execute(model: Model): String = JSONUtil.toJSON(model,JSonFormats.modelFormats)
}
