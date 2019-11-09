package com.github.notyy.typeflow.editor

import scala.util.Try

object LoadPureFunctionCodeTemplate {
  def execute(codeLang: CodeLang): Try[CodeTemplate] = {
    val path:Path = codeLang match {
      case JAVA_LANG => Path("./code_template/java/PureFunction.java")
      case _ => ???
    }
    ReadFile.execute(path).map(CodeTemplate)
  }
}
