package com.notyy.typeflow.example1

import com.notyy.typeflow.example1.UserInputInterpreter.{AddInputEndpointCommand, CreateModelCommand}
import org.scalatest.{FunSpec, Matchers}

class UserInputInterpreterTest extends FunSpec with Matchers {
  describe("UserInputInterpreter"){
    it("can understand add definition command"){
      //add (.*) (.*) haveOutputs (.*) toModel (.*)
      val result = UserInputInterpreter.execute("add InputEndpoint CommandLineInputEndpoint haveOutputs String toModel SampleModel")
      result shouldBe AddInputEndpointCommand("SampleModel","CommandLineInputEndpoint",OutputType("String"))
    }
    it("can understand create model command"){
      val result = UserInputInterpreter.execute("createModel sampleModel")
      result shouldBe CreateModelCommand("sampleModel")
    }
  }
}
