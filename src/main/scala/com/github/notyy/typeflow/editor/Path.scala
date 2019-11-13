package com.github.notyy.typeflow.editor

trait Path {
  def value: String
}

case class OutputPath(value: String) extends Path

case class ModelFilePath(value: String) extends Path
