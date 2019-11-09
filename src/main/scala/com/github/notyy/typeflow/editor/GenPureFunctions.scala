package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.PureFunction

class GenPureFunctions(val genJavaPureFunction: GenJavaPureFunction) {
  def execute(codeLang: CodeLang, pureFunctions: Vector[PureFunction],packageName: PackageName, codeTemplate: CodeTemplate): Vector[JavaCode] = {
    codeLang match {
      case JAVA_LANG => pureFunctions.map(pf => genJavaPureFunction.execute(packageName, pf, codeTemplate))
      case SCALA_LANG => ???
    }
  }
}
