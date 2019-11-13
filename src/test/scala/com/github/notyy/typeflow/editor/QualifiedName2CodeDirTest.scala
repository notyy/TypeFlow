package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class QualifiedName2CodeDirTest extends FunSpec with Matchers {
  describe("QualifiedName2CodeDir") {
    it("can extract dir names by given qualifiedName") {
      val codeDir = QualifiedName2CodeDir.execute(QualifiedName("com.github.notyy.editor.Abc"))
      codeDir.value shouldBe "com/github/notyy/editor"
    }
  }
}
