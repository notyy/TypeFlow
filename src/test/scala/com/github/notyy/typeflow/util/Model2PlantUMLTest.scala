package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.Fixtures
import com.github.notyy.typeflow.editor.{Path, SaveToFile}
import com.typesafe.scalalogging.Logger
import org.scalatest.{FunSpec, Matchers}

class Model2PlantUMLTest extends FunSpec with Matchers {
  private val logger = Logger(getClass)
  val model = Fixtures.model

  describe("Model2PlantUML") {
    it("can transform typeflow model to plantuml") {
      val plantUML = Model2PlantUML.execute(model).value
      SaveToFile.execute(Path("./localoutput/current.puml"),plantUML)
      logger.debug(s"plantUML==${System.lineSeparator()}$plantUML")
      plantUML shouldBe """
        |@startuml
        |class UserInputEndpoint <<InputEndpoint>>
        |class UserInputInterpreter <<PureFunction>>
        |class WrapOutput <<PureFunction>>
        |class CommandLineOutputEndpoint <<OutputEndpoint>>
        |class CreateNewModel <<OutputEndpoint>>
        |class AddDefinition <<PureFunction>>
        |class Model2Json <<PureFunction>>
        |class GetModelPath <<PureFunction>>
        |class SaveToFile <<OutputEndpoint>>
        |class OnSaveModelSuccess <<PureFunction>>
        |
        |UserInputEndpoint --> UserInput
        |UserInput --> UserInputInterpreter
        |UserInputInterpreter --> UnknownCommand
        |UnknownCommand --> WrapOutput
        |UserInputInterpreter --> QuitCommand
        |QuitCommand --> WrapOutput
        |UserInputInterpreter --> CreateModelCommand
        |CreateModelCommand --> CreateNewModel
        |CreateNewModel --> ModelCreationSuccess
        |ModelCreationSuccess --> WrapOutput
        |UserInputInterpreter --> AddInputEndpointCommand
        |AddInputEndpointCommand --> AddDefinition
        |AddDefinition --> Model
        |Model --> Model2Json
        |Model --> GetModelPath
        |GetModelPath --> Path
        |Path --> SaveToFile
        |Model2Json --> java.lang.String
        |java.lang.String --> SaveToFile
        |Model --> OnSaveModelSuccess
        |SaveToFile --> Unit
        |Unit --> OnSaveModelSuccess
        |OnSaveModelSuccess --> ModelUpdateSuccess
        |ModelUpdateSuccess --> WrapOutput
        |WrapOutput --> WrappedOutput
        |WrappedOutput --> CommandLineOutputEndpoint
        |@enduml
        |""".stripMargin
    }
  }
}
