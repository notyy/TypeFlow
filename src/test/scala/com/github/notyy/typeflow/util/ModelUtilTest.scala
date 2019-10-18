package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.{Fixtures, domain}
import com.github.notyy.typeflow.domain.{Connection, Flow, PureFunction, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType}
import org.scalatest.{FunSpec, Matchers}

class ModelUtilTest extends FunSpec with Matchers {

  val model = Fixtures.model

  describe("ModelUtil") {
    it("can tell the concrete type of a definition") {
      ModelUtil.definitionType(InputEndpoint("xx", OutputType("xx"))) shouldBe "InputEndpoint"
      ModelUtil.definitionType(PureFunction("xx",Vector.empty, Vector.empty)) shouldBe "PureFunction"
      ModelUtil.definitionType(OutputEndpoint("xx", InputType("xx"), OutputType("xx"), Vector.empty)) shouldBe "OutputEndpoint"
    }
    it("can find out instance's outputType of given index") {
      ModelUtil.findOutputType(Fixtures.userInputInterpreter.name, 1, model) shouldBe "UnknownCommand"
    }
  }
}
