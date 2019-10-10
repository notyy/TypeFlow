package com.notyy.typeflow.example1

object UserInputInterpreter {
  sealed trait InterpreterResult
  case class UnknownCommand(input: String) extends InterpreterResult
  case object QuitCommand extends InterpreterResult
  case class CreateModelCommand(modelName: String) extends InterpreterResult

  sealed trait ModifyModelCommand extends InterpreterResult
  case class AddInputEndpointCommand(modelName: String, name: String, outputType: OutputType) extends ModifyModelCommand
  case class ConnectInstanceCommand(fromInstanceId: String, outputIndex: Int, toInstanceId: String, modelName: String,flowName: String) extends ModifyModelCommand

  private val CreateModelPattern = """createModel (.*)""".r
  private val AddDefinitionPattern = """add (.*) (.*) haveOutputs (.*) toModel (.*)""".r
  private val ConnectElementPattern = """connect from (.*)(.*) to (.*) inFlow (.*).(.*)""".r

  def execute(input: String): InterpreterResult = {
    input match {
      case ":q" => QuitCommand
      case (CreateModelPattern(modelName)) => CreateModelCommand(modelName)
      case (AddDefinitionPattern(definitionType, definitionName, outputTypes, modelName)) => definitionType match {
        case "InputEndpoint" => AddInputEndpointCommand(modelName,definitionName,OutputType(outputTypes))
      }
      case (ConnectElementPattern(fromInstanceId,outputIndex, toInstanceId, modelName,flowName)) => ConnectInstanceCommand(fromInstanceId,outputIndex.toInt, toInstanceId, modelName,flowName)
      case _ => UnknownCommand(input)
    }
  }
}
