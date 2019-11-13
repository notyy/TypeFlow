package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadOutputEndpointCodeTemplate {
  def execute(codeLang: CodeLang): Try[CodeTemplate] = {
    val path: CodeTemplatePath = codeLang match {
      case JAVA_LANG => CodeTemplatePath("./code_template/java/OutputEndpoint.java")
      case _ => ???
    }
    ReadFile.execute(path).map(CodeTemplate)
  }
}
