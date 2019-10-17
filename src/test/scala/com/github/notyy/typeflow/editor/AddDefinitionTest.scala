package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Model, Output, OutputType}
import org.scalatest.{FunSpec, Matchers}

class AddDefinitionTest extends FunSpec with Matchers {
  describe("AddDefinition"){
    it("allow's user to add new Definition to a given model"){
      val model = Model("testModel", Vector.empty,Vector.empty,None)
      val afterAddInputEndpoint = AddDefinition.execute(model, AddInputEndpointCommand("testModel","UserInputEndpoint",OutputType("UserInput")))
      afterAddInputEndpoint.definitions.head.name shouldBe "UserInputEndpoint"
      val afterAddFunction = AddDefinition.execute(afterAddInputEndpoint, AddFunctionCommand("testModel","UserInputInterpreter",
        inputs = Vector(Input(InputType("UserInput"),1)),
        outputs = Vector(
          Output(OutputType("QuitCommand"),1),
          Output(OutputType("UnknownCommand"), 2),
          Output(OutputType("AddDefinitionCommand"),3)
        )))
      afterAddFunction.definitions.size shouldBe 2
      afterAddFunction.definitions.exists(defi => defi.name == "UserInputEndpoint") shouldBe true
      afterAddFunction.definitions.exists(defi => defi.name == "UserInputInterpreter") shouldBe true
      val afterAddWrapoutFunction = AddDefinition.execute(afterAddFunction,AddFunctionCommand("testModel", "WrapOutput",
        inputs = Vector(Input(InputType("java.lang.Object"),1)),
        outputs = Vector(Output(OutputType("WrappedOutput"),1))
      ))
      afterAddWrapoutFunction.definitions.size shouldBe 3
      afterAddWrapoutFunction.definitions.exists(defi => defi.name == "WrapOutput") shouldBe true
      val afterAddOutputEndpoint = AddDefinition.execute(afterAddWrapoutFunction,
        AddOutputEndpointCommand("testModel","CommandLineOutputEndpoint", InputType("WrappedOutput"), OutputType("Unit"), Vector.empty))
      afterAddFunction.definitions.size shouldBe 2
      afterAddOutputEndpoint.definitions.size shouldBe 4
      afterAddOutputEndpoint.definitions.exists(defi => defi.name == "CommandLineOutputEndpoint") shouldBe true
    }
  }
}
