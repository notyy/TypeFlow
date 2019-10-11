package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.example1.UserInputInterpreter.QuitCommand
import org.scalatest.{FunSpec, Matchers}

class TypeUtilTest extends FunSpec with Matchers {
  describe("TypeUtil"){
    it("can extract short type name of an variable"){
      TypeUtil.getTypeShortName(QuitCommand) shouldBe "QuitCommand"
      TypeUtil.getTypeShortName("something") shouldBe "String"
    }
  }
}
