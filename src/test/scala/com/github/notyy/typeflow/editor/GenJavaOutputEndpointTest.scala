package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, OutputEndpoint, OutputType}
import org.scalatest.{FunSpec, Matchers}

class GenJavaOutputEndpointTest extends FunSpec with Matchers {
  describe("GenJavaOutputEndpoint") {
    it("should generate java code skeleton for output endpoint") {
      val packageName = PackageName("com.github.notyy.typeflow.editor")
      val print: OutputEndpoint = OutputEndpoint(
        "Print",
        Vector(Input(InputType("Integer"),1)),
        OutputType("Unit"),
        Vector.empty
      )
      val codeTemplate =
        """|package $PackageName$;
           |
           |public class $DefinitionName$ {
           |    public $ReturnType$ execute($Params$) {
           |        System.out.println(param1);
           |    }
           |}
           |""".stripMargin
      val genJavaOutputEndpoint = new GenJavaOutputEndpoint(new GenFormalParams)
      val printCode = genJavaOutputEndpoint.execute(packageName, print, CodeTemplate(codeTemplate))
      printCode.qualifiedName.value shouldBe "com.github.notyy.typeflow.editor.Print"
      printCode.content shouldBe
        """|package com.github.notyy.typeflow.editor;
           |
           |public class Print {
           |    public void execute(Integer param1) {
           |        System.out.println(param1);
           |    }
           |}
           |""".stripMargin
    }
  }
}
