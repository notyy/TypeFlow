package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.Fixtures
import com.typesafe.scalalogging.Logger
import org.scalatest.{FunSpec, Matchers}

class Model2PlantUMLTest extends FunSpec with Matchers {
  private val logger = Logger(getClass)
  val model = Fixtures.model

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
