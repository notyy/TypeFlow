package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class ReadFileFromResourceTest extends FunSpec with Matchers {
  describe("ReadFileFromResource") {
    it("can read file from resource and return String") {
      ReadFileFromResource.execute("/test.typeflow") shouldBe """{"some":"test"}"""
    }
  }
}
