package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadAliyunOSSOutputEndpointCodeTemplate {
  def execute(codeLang: CodeLang): Try[CodeTemplate] = {
    val path: CodeTemplatePath = codeLang match {
      case JAVA_LANG => ???
      case SCALA_LANG => CodeTemplatePath("./code_template/scala/AliyunOSSOutputEndpointTemplate.scala")
    }
    ReadFile.execute(path).map(CodeTemplate)
  }
}
