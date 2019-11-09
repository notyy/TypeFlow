package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class LoadPureFunctionCodeTemplateTest extends FunSpec with Matchers {
  describe("LoadPureFunctionCodeTemplate") {
    it("can load java pure function code template") {
      val codeTemplate: CodeTemplate = LoadPureFunctionCodeTemplate.execute(JAVA_LANG).get
      codeTemplate.value.isEmpty shouldNot be(true)
    }
  }
}
