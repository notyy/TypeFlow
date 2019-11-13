package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType, PureFunction}
import org.scalatest.{FunSpec, Matchers}

class GenJavaPureFunctionTest extends FunSpec with Matchers {
  describe("GenPureJavaFunction") {
    it("should generate java code skeleton for pure function") {
      val packageName = PackageName("com.github.notyy.typeflow.editor")
      val add2: PureFunction = PureFunction(
        "Add2",
        Vector(Input(InputType("Integer"),1)),
        Vector(Output(OutputType("Integer"),1))
      )
      val codeTemplate =
         """|package $PackageName$;
            |
            |public class $DefinitionName$ {
            |    public $ReturnType$ execute($Params$) {
            |        return null;
            |    }
            |}
            |""".stripMargin
      val genPureJavaFunction = new GenJavaPureFunction(new GenFormalParams)
      val add2Code = genPureJavaFunction.execute(packageName, add2, CodeTemplate(codeTemplate))
      add2Code.qualifiedName.value shouldBe "com.github.notyy.typeflow.editor.Add2"
      add2Code.content shouldBe
         """|package com.github.notyy.typeflow.editor;
            |
            |public class Add2 {
            |    public Integer execute(Integer param1) {
            |        return null;
            |    }
            |}
            |""".stripMargin
    }
  }
}
