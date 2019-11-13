package com.github.notyy.typeflow.editor

class QualifiedName2CodeStructurePath {
  def execute(qualifiedName: QualifiedName, codeLang: CodeLang): CodeStructurePath = {
    CodeStructurePath(s"${qualifiedName.value.replaceAllLiterally(".", "/")}.${CodeLang.str(codeLang)}")
  }
}
