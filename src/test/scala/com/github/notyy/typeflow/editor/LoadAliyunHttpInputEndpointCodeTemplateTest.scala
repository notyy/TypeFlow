package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class LoadAliyunHttpInputEndpointCodeTemplateTest extends FunSpec with Matchers {
  describe("LoadAliyunHttpInputEndpointCodeTemplate") {
    it("can load aliyun handler code template") {
      val codeTemplate: CodeTemplate = LoadAliyunHttpInputEndpointCodeTemplate.execute().get
      codeTemplate.value.isEmpty shouldNot be(true)
    }
  }
}
