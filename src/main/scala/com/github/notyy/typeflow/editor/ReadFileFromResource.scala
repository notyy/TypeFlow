package com.github.notyy.typeflow.editor

import scala.io.Source

object ReadFileFromResource {
  def execute(path: String): String = {
    Source.fromInputStream(this.getClass.getResourceAsStream(path)).mkString
  }
}
