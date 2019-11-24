package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}
import com.github.notyy.typeflow.editor.Model2Code.{CodeContent, CodeFileName}
import org.scalatest.{FunSpec, Matchers}

class Model2CodeTest extends FunSpec with Matchers {
  describe("Model2Scala") {
    it("can generate code for typeflow model") {
      val puml = ReadFile.execute(ModelFilePath("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute("newModel", puml)

      val packageName = "com.github.notyy.newModel"
      val codes: Map[CodeFileName, CodeContent] = Model2Code.execute(model, packageName, Model2Code.PLATFORM_LOCAL,SCALA_LANG)
      codes.size shouldBe 4
      codes("AddAndPrint.scala") shouldBe
        s"""|package $packageName
            |
            |object AddAndPrint {
            |  def execute(param2: Integer,param1: Integer): Unit = {
            |    ???
            |  }
            |}
            |""".stripMargin
    }
    it("can generate code for typeflow model for given platform") {
      val puml = ReadFile.execute(ModelFilePath("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute("newModel", puml)

      val packageName = "com.github.notyy.newModel"
      val codes: Map[CodeFileName, CodeContent] = Model2Code.execute(model, packageName, Model2Code.PLATFORM_ALIYUN,SCALA_LANG)
      codes.size shouldBe 7
      codes.get("aliyun/Add2Handler.scala").isDefined shouldBe(true)
    }
    it("can generate formal params for inputs in definition") {
      val oneInput: Vector[Input] = Vector(Input(InputType("Integer"), 1))
      Model2Code.genFormalParams(oneInput,SCALA_LANG) shouldBe "param1: Integer"
      val unitInput: Vector[Input] = Vector(Input(InputType("Unit"), 1))
      Model2Code.genFormalParams(unitInput,SCALA_LANG) shouldBe ""
      val emptyInput: Vector[Input] = Vector.empty
      Model2Code.genFormalParams(emptyInput,SCALA_LANG) shouldBe ""
      val twoInputs: Vector[Input] = Vector(Input(InputType("Integer"), 1), Input(InputType("String"), 2))
      Model2Code.genFormalParams(twoInputs,SCALA_LANG) shouldBe "param1: Integer,param2: String"
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
      Model2Code.genReturnType(oneOutput, SCALA_LANG) shouldBe "Integer"
      val unitOutput: Vector[Output] = Vector(Output(OutputType("Unit"), 1))
      Model2Code.genReturnType(unitOutput, SCALA_LANG) shouldBe "Unit"
      val emptyInput: Vector[Output] = Vector.empty
      Model2Code.genReturnType(emptyInput,SCALA_LANG) shouldBe "Unit"
      val twoOutputs: Vector[Output] = Vector(Output(OutputType("Integer"), 1), Output(OutputType("String"), 2))
      Model2Code.genReturnType(twoOutputs, SCALA_LANG) shouldBe "Object"
    }
  }
}
