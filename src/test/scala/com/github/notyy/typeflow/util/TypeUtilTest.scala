package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.InputType
import com.github.notyy.typeflow.editor.{Path, QuitCommand}
import org.scalatest.{FunSpec, Matchers}

class TypeUtilTest extends FunSpec with Matchers {
  describe("TypeUtil") {
    it("should use short name for user defined type, and qualified name for primitive type") {
      TypeUtil.getTypeName(QuitCommand) shouldBe "QuitCommand"
      TypeUtil.getTypeName("something") shouldBe "java.lang.String"
    }
    it("can extract short type name of an variable") {
      TypeUtil.getTypeShortName(QuitCommand) shouldBe "QuitCommand"
      TypeUtil.getTypeShortName("something") shouldBe "String"
    }
    it("can compose full type for given inputType") {
      TypeUtil.composeInputType(Some("xyz"),InputType("BizClass")) shouldBe "xyz.BizClass"
    }
    it("will reserve original type, if given inputType is qualified ") {
      TypeUtil.composeInputType(Some("xyz"),InputType("abc.BizClass")) shouldBe "abc.BizClass"
    }
    it("whould recognize java primitive types when composing type") {
      TypeUtil.composeInputType(Some("xyz"),InputType("String")) shouldBe "java.lang.String"
      TypeUtil.composeInputType(Some("xyz"),InputType("Object")) shouldBe "java.lang.Object"
    }
    it("AnyVal should be ok"){
      TypeUtil.getTypeShortName(Path("xxx")) shouldBe "Path"
    }
    it("can transform decorated type to origin type") {
      TypeUtil.removeDecorate("NI::Integer") shouldBe "Integer"
    }
  }
}
