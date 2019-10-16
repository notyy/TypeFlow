package com.github.notyy.typeflow

import com.github.notyy.typeflow.domain.{Connection, Flow, Input, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType}

object Fixtures {
  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("UserInput"))
  val userInputInterpreter: domain.Function = domain.Function("UserInputInterpreter", inputs = Vector(Input(InputType("UserInput"),1)),
    outputs = Vector(
      Output(OutputType("UnknownCommand"), 1),
      Output(OutputType("QuitCommand"), 2)
    ))
  val wrapOutput: domain.Function = domain.Function("WrapOutput", inputs = Vector(Input(InputType("java.lang.Object"),1)),
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
}
