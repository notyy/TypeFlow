package com.github.notyy.typeflow.editor

object ModelPath2ModelName {
  def execute(path: String): String = path.dropRight(5).split('/').last
}
