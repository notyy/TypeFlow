package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.UserInputInterpreter.AddInputEndpointCommand
import org.scalatest.{FunSpec, Matchers}

class UserInputInterpreterTest extends FunSpec with Matchers {
  describe("UserInputInterpreter"){
    it("can understand add definition command"){
      //add (.*) (.*) haveOutputs (.*) toModel (.*)
      val result = UserInputInterpreter.execute("add InputEndpoint CommandLineInputEndpoint haveOutputs String toModel SampleModel")
      result shouldBe(AddInputEndpointCommand("SampleModel","CommandLineInputEndpoint",OutputType("String")))
    }
  }
}
