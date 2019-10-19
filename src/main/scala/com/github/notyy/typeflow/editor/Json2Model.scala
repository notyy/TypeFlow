package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}

object Json2Model {
  def execute(json: String): Option[Model] = {
    JSONUtil.fromJSON[Model](json,JSonFormats.modelFormats).toOption
  }
}
