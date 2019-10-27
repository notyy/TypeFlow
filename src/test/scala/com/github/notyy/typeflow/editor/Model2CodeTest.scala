package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}
import com.github.notyy.typeflow.editor.Model2Code.{CodeContent, CodeFileName}
import org.scalatest.{FunSpec, Matchers}

class Model2CodeTest extends FunSpec with Matchers {
  describe("Model2Scala") {
    it("can generate code for typeflow model") {
      val puml = ReadFile.execute(Path("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute(PlantUML("newModel", puml))

      val packageName = "com.github.notyy.newModel"
      val codes: Map[CodeFileName, CodeContent] = Model2Code.execute(model, packageName, Model2Code.PLATFORM_LOCAL,LANG_SCALA)
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
    it("can generate code for typeflow model for given platform") {
      val puml = ReadFile.execute(Path("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute(PlantUML("newModel", puml))

      val packageName = "com.github.notyy.newModel"
      val codes: Map[CodeFileName, CodeContent] = Model2Code.execute(model, packageName, Model2Code.PLATFORM_ALIYUN,LANG_SCALA)
      codes.size shouldBe 7
      codes.get("aliyun/Add2Handler.scala").isDefined shouldBe(true)
    }
    it("can generate formal params for inputs in definition") {
      val oneInput: Vector[Input] = Vector(Input(InputType("Integer"), 1))
      Model2Code.genFormalParams(oneInput,LANG_SCALA) shouldBe "param1: Integer"
      val unitInput: Vector[Input] = Vector(Input(InputType("Unit"), 1))
      Model2Code.genFormalParams(unitInput,LANG_SCALA) shouldBe ""
      val emptyInput: Vector[Input] = Vector.empty
      Model2Code.genFormalParams(emptyInput,LANG_SCALA) shouldBe ""
      val twoInputs: Vector[Input] = Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))
      Model2Code.genFormalParams(twoInputs,LANG_SCALA) shouldBe "param1: Integer,param2: String"
    }
    it("can generate actual params for inputs in definition") {
      val oneInput: Vector[Input] = Vector(Input(InputType("Integer"), 1))
      Model2Code.genActualParams(oneInput) shouldBe "Integer"
      val unitInput: Vector[Input] = Vector(Input(InputType("Unit"), 1))
      Model2Code.genActualParams(unitInput) shouldBe ""
      val emptyInput: Vector[Input] = Vector.empty
      Model2Code.genActualParams(emptyInput) shouldBe ""
      val twoInputs: Vector[Input] = Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))
      Model2Code.genActualParams(twoInputs) shouldBe "(Integer,String)"
    }
    it("can generate paramCalls") {
      val oneInput: Vector[Input] = Vector(Input(InputType("Integer"), 1))
      Model2Code.genParamCall(oneInput) shouldBe "param.value"
      val twoInputs: Vector[Input] = Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))
      Model2Code.genParamCall(twoInputs) shouldBe "param.value._1,param.value._2"
    }
    it("can generate return type for outputs in definition") {
      val oneOutput: Vector[Output] = Vector(Output(OutputType("Integer"), 1))
      Model2Code.genReturnType(oneOutput, LANG_SCALA) shouldBe "Integer"
      val unitOutput: Vector[Output] = Vector(Output(OutputType("Unit"), 1))
      Model2Code.genReturnType(unitOutput, LANG_SCALA) shouldBe "Unit"
      val emptyInput: Vector[Output] = Vector.empty
      Model2Code.genReturnType(emptyInput,LANG_SCALA) shouldBe "Unit"
      val twoOutputs: Vector[Output] = Vector(Output(OutputType("Integer"), 1), Output(OutputType("String"), 2))
      Model2Code.genReturnType(twoOutputs, LANG_SCALA) shouldBe "Object"
    }
  }
}
