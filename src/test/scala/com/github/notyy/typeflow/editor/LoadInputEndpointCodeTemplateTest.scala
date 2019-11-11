package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class LoadInputEndpointCodeTemplateTest extends FunSpec with Matchers {
  describe("LoadInputEndpointCodeTemplate") {
    it("can load command line input endpoint code template") {
      val codeTemplate: CodeTemplate = LoadInputEndpointCodeTemplate.execute(JAVA_LANG).get
      codeTemplate.value.isEmpty shouldNot be(true)
    }
  }
}
