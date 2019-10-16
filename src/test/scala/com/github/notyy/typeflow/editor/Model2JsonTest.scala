package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain
import com.github.notyy.typeflow.domain.{Connection, Flow, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType}
import org.scalatest.{FunSpec, Matchers}

class Model2JsonTest extends FunSpec with Matchers {
  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("UserInput"))
  val userInputInterpreter: domain.Function = domain.Function("UserInputInterpreter", InputType("UserInput"),
    outputs = Vector(
      Output(OutputType("UnknownCommand"), 1),
      Output(OutputType("QuitCommand"), 2)
    ))
  val wrapOutput: domain.Function = domain.Function("WrapOutput", InputType("java.lang.Object"),
    outputs = Vector(Output(OutputType("WrappedOutput"), 1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", InputType("WrappedOutput"), OutputType("Unit"), Vector.empty)
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

  describe("Model2Json"){
    it("transform Model to Json string") {
      val json = Model2Json.execute(model)
      println(json)
      json shouldBe """{"name":"typeflow_editor","definitions":[{"jsonClass":"InputEndpoint","name":"UserInputEndpoint","outputType":{"name":"UserInput"}},{"jsonClass":"Function","name":"UserInputInterpreter","inputType":{"name":"UserInput"},"outputs":[{"outputType":{"name":"UnknownCommand"},"index":1},{"outputType":{"name":"QuitCommand"},"index":2}]},{"jsonClass":"Function","name":"WrapOutput","inputType":{"name":"java.lang.Object"},"outputs":[{"outputType":{"name":"WrappedOutput"},"index":1}]},{"jsonClass":"OutputEndpoint","name":"CommandLineOutputEndpoint","inputType":{"name":"WrappedOutput"},"outputType":{"name":"Unit"},"errorOutput":[]}],"flows":[{"name":"minimalFlow","instances":[{"id":"UserInputEndpoint","definition":{"jsonClass":"InputEndpoint","name":"UserInputEndpoint","outputType":{"name":"UserInput"}}},{"id":"UserInputInterpreter","definition":{"jsonClass":"Function","name":"UserInputInterpreter","inputType":{"name":"UserInput"},"outputs":[{"outputType":{"name":"UnknownCommand"},"index":1},{"outputType":{"name":"QuitCommand"},"index":2}]}},{"id":"WrapOutput","definition":{"jsonClass":"Function","name":"WrapOutput","inputType":{"name":"java.lang.Object"},"outputs":[{"outputType":{"name":"WrappedOutput"},"index":1}]}},{"id":"CommandLineOutputEndpoint","definition":{"jsonClass":"OutputEndpoint","name":"CommandLineOutputEndpoint","inputType":{"name":"WrappedOutput"},"outputType":{"name":"Unit"},"errorOutput":[]}}],"connections":[{"fromInstanceId":"UserInputEndpoint","outputIndex":1,"toInstanceId":"UserInputInterpreter"},{"fromInstanceId":"UserInputInterpreter","outputIndex":1,"toInstanceId":"WrapOutput"},{"fromInstanceId":"UserInputInterpreter","outputIndex":2,"toInstanceId":"WrapOutput"},{"fromInstanceId":"WrapOutput","outputIndex":1,"toInstanceId":"CommandLineOutputEndpoint"}]}],"activeFlow":{"name":"minimalFlow","instances":[{"id":"UserInputEndpoint","definition":{"jsonClass":"InputEndpoint","name":"UserInputEndpoint","outputType":{"name":"UserInput"}}},{"id":"UserInputInterpreter","definition":{"jsonClass":"Function","name":"UserInputInterpreter","inputType":{"name":"UserInput"},"outputs":[{"outputType":{"name":"UnknownCommand"},"index":1},{"outputType":{"name":"QuitCommand"},"index":2}]}},{"id":"WrapOutput","definition":{"jsonClass":"Function","name":"WrapOutput","inputType":{"name":"java.lang.Object"},"outputs":[{"outputType":{"name":"WrappedOutput"},"index":1}]}},{"id":"CommandLineOutputEndpoint","definition":{"jsonClass":"OutputEndpoint","name":"CommandLineOutputEndpoint","inputType":{"name":"WrappedOutput"},"outputType":{"name":"Unit"},"errorOutput":[]}}],"connections":[{"fromInstanceId":"UserInputEndpoint","outputIndex":1,"toInstanceId":"UserInputInterpreter"},{"fromInstanceId":"UserInputInterpreter","outputIndex":1,"toInstanceId":"WrapOutput"},{"fromInstanceId":"UserInputInterpreter","outputIndex":2,"toInstanceId":"WrapOutput"},{"fromInstanceId":"WrapOutput","outputIndex":1,"toInstanceId":"CommandLineOutputEndpoint"}]}}"""
    }
  }
}