package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadInputEndpointCodeTemplate {
  def execute(codeLang: CodeLang): Try[CodeTemplate] = {
    val path:Path = codeLang match {
      case _ => Path("./code_template/scala/CommandLineInputEndpoint.scala")
    }
    ReadFile.execute(path).map(CodeTemplate)
  }
}
