package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadAliyunHttpInputEndpointCodeTemplate {
  def execute(): Try[CodeTemplate] = {
    val path: CodeTemplatePath = CodeTemplatePath("./code_template/scala/AliyunHttpInputEndpoint.scala")
    ReadFile.execute(path).map(CodeTemplate)
  }
}
