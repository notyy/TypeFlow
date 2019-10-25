package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}
import com.github.notyy.typeflow.editor.Model2Scala.{CodeContent, CodeFileName}
import org.scalatest.{FunSpec, Matchers}

class Model2ScalaTest extends FunSpec with Matchers {
  describe("Model2Scala") {
    it("can generate code for typeflow model") {
      val puml = ReadFile.execute(Path("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute(PlantUML("newModel", puml))

      val packageName = "com.github.notyy.newModel"
      val codes: Map[CodeFileName, CodeContent] = Model2Scala.execute(model, packageName)
      codes.size shouldBe 4
      codes("NumInput.scala").contains("LocalRunEngine.runFlow") shouldBe true
      codes("AddAndPrint.scala") shouldBe
        s"""|package $packageName
            |
            |object AddAndPrint {
            |  def execute(param1: Integer,param2: Integer): Unit = {
            |    ???
            |  }
            |}
            |""".stripMargin
    }
    it("can generate params for inputs in definition") {
      val oneInput: Vector[Input] = Vector(Input(InputType("Integer"), 1))
      Model2Scala.genParams(oneInput) shouldBe "param1: Integer"
      val unitInput: Vector[Input] = Vector(Input(InputType("Unit"), 1))
      Model2Scala.genParams(unitInput) shouldBe ""
      val emptyInput: Vector[Input] = Vector.empty
      Model2Scala.genParams(emptyInput) shouldBe ""
      val twoInputs: Vector[Input] = Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))
      Model2Scala.genParams(twoInputs) shouldBe "param1: Integer,param2: String"
    }
    it("can generate return type for outputs in definition") {
      val oneOutput: Vector[Output] = Vector(Output(OutputType("Integer"), 1))
      Model2Scala.genReturnType(oneOutput) shouldBe "Integer"
      val unitOutput: Vector[Output] = Vector(Output(OutputType("Unit"), 1))
      Model2Scala.genReturnType(unitOutput) shouldBe "Unit"
      val emptyInput: Vector[Output] = Vector.empty
      Model2Scala.genReturnType(emptyInput) shouldBe "Unit"
      val twoOutputs: Vector[Output] = Vector(Output(OutputType("Integer"), 1), Output(OutputType("String"), 2))
      Model2Scala.genReturnType(twoOutputs) shouldBe "Any"
    }
  }
}
