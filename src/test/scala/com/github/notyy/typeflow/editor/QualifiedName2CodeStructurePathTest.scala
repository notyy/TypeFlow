package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class QualifiedName2CodeStructurePathTest extends FunSpec with Matchers {
  describe("QualifiedName2CodeStructurePath") {
    it("can transform qualified name to code structure path") {
      val path = new QualifiedName2CodeStructurePath().execute(QualifiedName("com.github.notyy.typeflow.editor.Abc"), JAVA_LANG)
      path.value shouldBe "com/github/notyy/typeflow/editor/Abc.java"
    }
  }
}
