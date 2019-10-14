package com.github.notyy.typeflow.editor

import UserInputInterpreter.{AddInputEndpointCommand, CreateModelCommand}
import com.github.notyy.typeflow.domain.OutputType
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
