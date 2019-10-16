package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Model, OutputType}
import org.scalatest.{FunSpec, Matchers}

class AddDefinitionTest extends FunSpec with Matchers {
  describe("AddDefinition"){
    it("allow's user to add new Definition to a given model"){
      val model = Model("testModel", Vector.empty,Vector.empty,None)
      val newModel = AddDefinition.execute(model, AddInputEndpointCommand("testModel","UserInputEndpoint",OutputType("InputType")))
      newModel.definitions.head.name shouldBe "UserInputEndpoint"
    }
  }
}
