package com.github.notyy.typeflow.editor

import scala.io.Source

object ReadFile {
  def execute(path: String): String = {
    val source = Source.fromFile(path)
    val rs = source.mkString
    source.close()
    rs
  }
}
