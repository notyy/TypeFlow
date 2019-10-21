package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}

import scala.util.Try

object Json2Model {
  def execute(json: String): Try[Model] = {
    JSONUtil.fromJSON[Model](json,JSonFormats.modelFormats)
  }
}
