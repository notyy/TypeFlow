package com.github.notyy.typeflow.editor

import scala.io.Source

object ReadFile {
  def execute(path: Path): String = {
    val source = Source.fromFile(path.value)
    val rs = source.mkString
    source.close()
    rs
  }
}
