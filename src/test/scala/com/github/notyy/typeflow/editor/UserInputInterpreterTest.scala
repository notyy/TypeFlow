package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}
import org.scalatest.{FunSpec, Matchers}

class UserInputInterpreterTest extends FunSpec with Matchers {
  describe("UserInputInterpreter") {
    it("can understand add InputEndpoint command") {
      //add (.*) (.*) haveOutputs (.*) toModel (.*)
      val result = UserInputInterpreter.execute(UserInput("add InputEndpoint UserInputEndpoint haveOutputType UserInput toModel SampleModel"))
      result shouldBe AddInputEndpointCommand("SampleModel", "UserInputEndpoint", OutputType("UserInput"))
    }
    it("can understand add Function command") {
      //add Function (.*) haveInputs (.*) haveOutputs (.*) toModel (.*)
      val result = UserInputInterpreter.execute(
        UserInput("add Function UserInputInterpreter haveInputs UserInput,1 haveOutputs UnknownCommand,1;QuitCommand,2;CreateModelCommand,3;AddInputEndpointCommand,4;AddFunctionCommand,5;AddOutputEndpointCommand,6 toModel SampleModel"))
      result shouldBe AddFunctionCommand("SampleModel", "UserInputInterpreter", Vector(Input(InputType("UserInput"), 1)),
        outputs = Vector(
          Output(OutputType("UnknownCommand"), 1),
          Output(OutputType("QuitCommand"), 2),
          Output(OutputType("CreateModelCommand"), 3),
          Output(OutputType("AddInputEndpointCommand"), 4),
          Output(OutputType("AddFunctionCommand"), 5),
          Output(OutputType("AddOutputEndpointCommand"), 6)
        )
      )
    }
    it("can extract inputs from user input string") {
      UserInputInterpreter.extractInputs("UserInput,1;x,2") shouldBe Vector(Input(InputType("UserInput"), 1), Input(InputType("x"), 2))
    }
    it("can extract outputs from user input string") {
      UserInputInterpreter.extractOutputs("UnknownCommand,1;QuitCommand,2;CreateModelCommand,3;AddInputEndpointCommand,4;AddFunctionCommand,5;AddOutputEndpointCommand,6") shouldBe
        Vector(
          Output(OutputType("UnknownCommand"), 1),
          Output(OutputType("QuitCommand"), 2),
          Output(OutputType("CreateModelCommand"), 3),
          Output(OutputType("AddInputEndpointCommand"), 4),
          Output(OutputType("AddFunctionCommand"), 5),
          Output(OutputType("AddOutputEndpointCommand"), 6)
        )
    }
    it("can understand create model command") {
      val result = UserInputInterpreter.execute(UserInput("createModel sampleModel"))
      result shouldBe CreateModelCommand("sampleModel")
    }
  }
}
