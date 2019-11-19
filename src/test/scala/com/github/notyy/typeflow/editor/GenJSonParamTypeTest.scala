package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType}
import org.scalatest.{FunSpec, Matchers}

class GenJSonParamTypeTest extends FunSpec with Matchers {
  describe("GenJSonParamType") {
    it("generate parameter type to be used by json") {
      val genJSonParamType = new GenJSonParamType
      genJSonParamType.execute(Vector(Input(InputType("Integer"), 1))) shouldBe "Integer"
      genJSonParamType.execute(Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))) shouldBe "(Integer,String)"
    }
  }
}
