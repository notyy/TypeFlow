package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.OutputType

object UserInputInterpreter {

  private val CreateModelPattern = """createModel (.*)""".r
  private val AddDefinitionPattern = """add (.*) (.*) haveOutputs (.*) toModel (.*)""".r
  private val ConnectElementPattern = """connect from (.*)(.*) to (.*) inFlow (.*).(.*)""".r

  def execute(input: UserInput): InterpreterResult = {
    input.value match {
      case ":q" => QuitCommand
      case (CreateModelPattern(modelName)) => CreateModelCommand(modelName)
      case (AddDefinitionPattern(definitionType, definitionName, outputTypes, modelName)) => definitionType match {
        case "InputEndpoint" => AddInputEndpointCommand(modelName,definitionName,OutputType(outputTypes))
      }
      case (ConnectElementPattern(fromInstanceId,outputIndex, toInstanceId, modelName,flowName)) => ConnectInstanceCommand(fromInstanceId,outputIndex.toInt, toInstanceId, modelName,flowName)
      case _ => UnknownCommand(input.value)
    }
  }
}
