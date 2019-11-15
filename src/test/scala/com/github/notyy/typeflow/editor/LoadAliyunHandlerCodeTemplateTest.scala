package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class LoadAliyunHandlerCodeTemplateTest extends FunSpec with Matchers {
  describe("LoadAliyunHandlerCodeTemplate") {
    it("can load aliyun handler code template") {
      val codeTemplate: CodeTemplate = LoadAliyunHandlerCodeTemplate.execute().get
      codeTemplate.value.isEmpty shouldNot be(true)
    }
  }
}
