package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadAliyunHandlerCodeTemplate {
  def execute(): Try[CodeTemplate] = {
    val path: CodeTemplatePath = CodeTemplatePath("./code_template/scala/StreamRequestHandlerTemplate.scala")
    ReadFile.execute(path).map(CodeTemplate)
  }
}
