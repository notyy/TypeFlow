package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.{Fixtures, domain}
import com.github.notyy.typeflow.domain.{CommandLineInputEndpoint, Connection, Flow, Input, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType, PureFunction}
import org.scalatest.{FunSpec, Matchers}

class ModelUtilTest extends FunSpec with Matchers {

  val model = Fixtures.model

  describe("ModelUtil") {
    it("can tell the concrete type of a definition") {
      ModelUtil.definitionType(CommandLineInputEndpoint("xx", OutputType("xx"))) shouldBe "CommandLineInputEndpoint"
      ModelUtil.definitionType(PureFunction("xx",Vector.empty, Vector.empty)) shouldBe "PureFunction"
      ModelUtil.definitionType(OutputEndpoint("xx", Vector(Input(InputType("xx"),1)), OutputType("xx"), Vector.empty)) shouldBe "OutputEndpoint"
    }
    it("can find out instance's outputType of given index and remove prefix if exists") {
      ModelUtil.findOutputTypeRemovePrefix(Fixtures.userInputInterpreter.name, 1, model) shouldBe Some("UnknownCommand")
      ModelUtil.findOutputTypeRemovePrefix(Fixtures.getModelPath.name, 3, model) shouldBe None
    }
    it("can find out instance's outputType of given index and keep as is") {
      ModelUtil.findOutputType(Fixtures.userInputInterpreter.name, 1, model) shouldBe Some("UII::UnknownCommand")
    }
    it("can find out instance's outputType, even if it's an qualified typename") {
      ModelUtil.findOutputType(Fixtures.json2Model.name, 1, model) shouldBe Some("J2M::com.github.notyy.typeflow.domain.Model")
    }
    it("can find out instance's outputType, even if it's an qualified typename and only take short name") {
      ModelUtil.findOutputTypeRemovePrefixShortName(Fixtures.json2Model.name, 1, model) shouldBe Some("Model")
    }
  }
}
