package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain
import com.github.notyy.typeflow.domain.{Connection, Flow, Function, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType}
import org.scalatest.{FunSpec, Matchers}

class ModelUtilTest extends FunSpec with Matchers {

  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("java.lang.String"))
  val userInputInterpreter: domain.Function = domain.Function("UserInputInterpreter", InputType("java.lang.String"),
    outputs = Vector(
      Output(OutputType("UnknownCommand"), 1),
      Output(OutputType("QuitCommand"), 2)
    ))
  val wrapOutput: domain.Function = domain.Function("WrapOutput", InputType("java.lang.Object"),
    outputs = Vector(Output(OutputType("java.lang.String"), 1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", InputType("java.lang.String"), OutputType("Unit"), Vector.empty)
  val minimalFlow: Flow = Flow("minimalFlow",
    instances = Vector(
      //use definition name as default instance id
      Instance(userInputEndpoint),
      Instance(userInputInterpreter),
      Instance(wrapOutput),
      Instance(outputEndpoint)
    ),
    connections = Vector(
      Connection(userInputEndpoint.name, 1, userInputInterpreter.name),
      Connection(userInputInterpreter.name, 1, wrapOutput.name),
      Connection(userInputInterpreter.name, 2, wrapOutput.name),
      Connection(wrapOutput.name, 1, outputEndpoint.name)
    )
  )
  val model: Model = domain.Model("typeflow_editor", Vector(userInputEndpoint, userInputInterpreter, wrapOutput, outputEndpoint), Vector(minimalFlow), minimalFlow)

  describe("ModelUtil") {
    it("can tell the concrete type of a definition") {
      ModelUtil.definitionType(InputEndpoint("xx", OutputType("xx"))) shouldBe "InputEndpoint"
      ModelUtil.definitionType(Function("xx", InputType("xx"), Vector.empty)) shouldBe "Function"
      ModelUtil.definitionType(OutputEndpoint("xx", InputType("xx"), OutputType("xx"), Vector.empty)) shouldBe "OutputEndpoint"
    }
    it("can find out instance's outputType of given index") {
      ModelUtil.findOutputType(userInputInterpreter.name, 1, model) shouldBe "UnknownCommand"
    }
  }
}
