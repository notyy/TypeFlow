package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class DefinitionSorterTest extends FunSpec with Matchers {
  describe("DefinitionSorter") {
    it("will sort definitions by type, for later code generation") {
      val puml = ReadFile.execute(ModelFilePath("./fixtures/diff/newModel.puml")).get
      val model = PlantUML2Model.execute("newModel", puml)
      val (pureFunctions, inputEndpoints, outputEndpoints, customTypes, fileOutputEndpoints, aliyunOSSOutputEndpoints) = DefinitionSorter.execute(model)
      pureFunctions.size shouldBe 2
      pureFunctions.exists(_.name == "Add2") shouldBe true
      pureFunctions.exists(_.name == "Multi3") shouldBe true
      inputEndpoints.size shouldBe 1
      inputEndpoints.exists(_.name == "NumInput") shouldBe true
      outputEndpoints.size shouldBe 1
      outputEndpoints.exists(_.name == "AddAndPrint") shouldBe true
      customTypes.isEmpty shouldBe true
      fileOutputEndpoints.isEmpty shouldBe true
      aliyunOSSOutputEndpoints.isEmpty shouldBe true
    }
  }
}
