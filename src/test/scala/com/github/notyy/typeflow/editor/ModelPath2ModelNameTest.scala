package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class ModelPath2ModelNameTest extends FunSpec with Matchers {
  describe("ModelPath2ModelName") {
    it("use simple name(without folder and ext) of the model path as the default model name") {
      ModelPath2ModelName.execute("./fixtures/newModel.puml") shouldBe "newModel"
    }
  }
}
