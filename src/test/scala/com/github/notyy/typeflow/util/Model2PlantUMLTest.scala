package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain
import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger
import org.scalatest.{FunSpec, Matchers}

class Model2PlantUMLTest extends FunSpec with Matchers {
  private val logger = Logger(getClass)
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

  describe("Model2PlantUML") {
    it("can transform typeflow model to plantuml") {
      val plantUML = Model2PlantUML.execute(model).value
      logger.debug(s"plantUML==${System.lineSeparator()}$plantUML")
      plantUML shouldBe """
        |@startuml
        |class UserInputEndpoint <<InputEndpoint>>
        |class UserInputInterpreter <<Function>>
        |class WrapOutput <<Function>>
        |class CommandLineOutputEndpoint <<OutputEndpoint>>
        |
        |UserInputEndpoint --> UserInput
        |UserInput --> UserInputInterpreter
        |UserInputInterpreter --> UnknownCommand
        |UnknownCommand --> WrapOutput
        |UserInputInterpreter --> QuitCommand
        |QuitCommand --> WrapOutput
        |WrapOutput --> WrappedOutput
        |WrappedOutput --> CommandLineOutputEndpoint
        |@enduml
        |""".stripMargin
    }
  }
}
