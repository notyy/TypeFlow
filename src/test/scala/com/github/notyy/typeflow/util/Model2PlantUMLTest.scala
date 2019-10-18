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
        |class Command2ModelName <<PureFunction>>
        |class ReadFile <<OutputEndpoint>>
        |class Json2Model <<PureFunction>>
        |
        |UserInputEndpoint --> UIE::UserInput
        |UIE::UserInput --> UserInputInterpreter
        |UserInputInterpreter --> UII::UnknownCommand
        |UII::UnknownCommand --> WrapOutput
        |UserInputInterpreter --> UII::QuitCommand
        |UII::QuitCommand --> WrapOutput
        |UserInputInterpreter --> UII::CreateModelCommand
        |UII::CreateModelCommand --> CreateNewModel
        |CreateNewModel --> CNM::com.github.notyy.typeflow.domain.ModelCreationSuccess
        |CNM::com.github.notyy.typeflow.domain.ModelCreationSuccess --> WrapOutput
        |UserInputInterpreter --> UII::AddInputEndpointCommand
        |UII::AddInputEndpointCommand --> AddDefinition
        |UII::AddInputEndpointCommand --> Command2ModelName
        |Command2ModelName --> C2MN::String
        |C2MN::String --> ReadFile
        |ReadFile --> RF::String
        |RF::String --> Json2Model
        |Json2Model --> J2M::com.github.notyy.typeflow.domain.Model
        |J2M::com.github.notyy.typeflow.domain.Model --> AddDefinition
        |AddDefinition --> com.github.notyy.typeflow.domain.Model
        |com.github.notyy.typeflow.domain.Model --> Model2Json
        |com.github.notyy.typeflow.domain.Model --> GetModelPath
        |GetModelPath --> GMP::Path
        |GMP::Path --> SaveToFile
        |Model2Json --> M2J::String
        |M2J::String --> SaveToFile
        |com.github.notyy.typeflow.domain.Model --> OnSaveModelSuccess
        |SaveToFile --> STF::Unit
        |STF::Unit --> OnSaveModelSuccess
        |OnSaveModelSuccess --> OSMS::com.github.notyy.typeflow.domain.ModelUpdateSuccess
        |OSMS::com.github.notyy.typeflow.domain.ModelUpdateSuccess --> WrapOutput
        |WrapOutput --> WO::WrappedOutput
        |WO::WrappedOutput --> CommandLineOutputEndpoint
        |@enduml
        |""".stripMargin
    }
  }
}
