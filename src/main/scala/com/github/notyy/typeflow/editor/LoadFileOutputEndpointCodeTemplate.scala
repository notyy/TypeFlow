package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadFileOutputEndpointCodeTemplate {
  def execute(codeLang: CodeLang): Try[CodeTemplate] = {
    val path: CodeTemplatePath = codeLang match {
      case JAVA_LANG => CodeTemplatePath("./code_template/java/FileOutputEndpointTemplate.java")
      case _ => ???
    }
    ReadFile.execute(path).map(CodeTemplate)
  }
}
