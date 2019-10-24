package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}
import com.github.notyy.typeflow.domain.{Input, InputEndpoint, InputType, PureFunction, OutputEndpoint}

class PlantUML2ModelTest extends FunSpec with Matchers {
  describe("PlantUML2Model") {
    it("can transform from simple plantuml string to type flow model") {
      val puml = ReadFile.execute(Path("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute(PlantUML("newModel",puml))
      model.name shouldBe "newModel"
      model.definitions.size shouldBe 4
      model.definitions.map(_.name) should contain("NumInput")
      model.definitions.map(_.name) should contain("Add2")
      model.definitions.map(_.name) should contain("Multi3")
      model.definitions.map(_.name) should contain("AddAndPrint")
      val numInput: InputEndpoint = model.definitions.find(_.name == "NumInput").get.asInstanceOf[InputEndpoint]
      numInput.outputType.name shouldBe "NI::Integer"
      val add2: PureFunction = model.definitions.find(_.name == "Add2").get.asInstanceOf[PureFunction]
      add2.inputs.size shouldBe 1
      add2.inputs.head shouldBe Input(InputType("Integer"),1)
      val addAndPrint: OutputEndpoint = model.definitions.find(_.name == "AddAndPrint").get.asInstanceOf[OutputEndpoint]
      addAndPrint.inputs.size shouldBe 2
      addAndPrint.inputs.head shouldBe Input(InputType("Integer"),1)
      addAndPrint.inputs.last shouldBe Input(InputType("Integer"),2)
    }
  }
}
