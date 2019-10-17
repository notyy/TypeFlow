package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}
import com.typesafe.scalalogging.Logger

object UserInputInterpreter {
  private val logger = Logger("UserInputInterpreter")

  private val CreateModelPattern = """createModel (.*)""".r
  private val AddInputEndpointPattern = """add InputEndpoint (.*) haveOutputType (.*) toModel (.*)""".r
  private val AddFunctionPattern = """add Function (.*) haveInputs (.*) haveOutputs (.*) toModel (.*)""".r
  private val AddOutputEndpointPattern = """add OutputEndpoint (.*) haveInputType (.*) haveOutputType (.*) haveErrorOutputs (.*) toModel (.*)""".r
  private val ConnectElementPattern = """connect from (.*)(.*) to (.*) inFlow (.*).(.*)""".r

  def execute(input: UserInput): InterpreterResult = {
    input.value match {
      case ":q" => QuitCommand
      case CreateModelPattern(modelName) => CreateModelCommand(modelName)
      case AddInputEndpointPattern(name, outputType, modelName) => AddInputEndpointCommand(modelName, name, OutputType(outputType))
      case AddFunctionPattern(name, inputs, outputs, modelName) => AddFunctionCommand(modelName, name, extractInputs(inputs), extractOutputs(outputs))
      case AddOutputEndpointPattern(name, inputType, outputType, errorOutputs, modelName) => {
        AddOutputEndpointCommand(modelName,name,InputType(inputType), OutputType(outputType),
          if(errorOutputs == "Empty") Vector.empty else extractOutputs(errorOutputs))
      }
      case ConnectElementPattern(fromInstanceId,outputIndex, toInstanceId, modelName,flowName) => ConnectInstanceCommand(fromInstanceId,outputIndex.toInt, toInstanceId, modelName,flowName)
      case _ => UnknownCommand(input.value)
    }
  }

  def extractInputs(inputs: String): Vector[Input] = {
    inputs.split(";").map{ part =>
      val sect = part.split(",")
      logger.debug(s"sect=$sect")
      Input(InputType(sect.head), sect.last.toInt)
    }.toVector
  }

  def extractOutputs(outputs: String): Vector[Output] = {
    outputs.split(";").map{ part =>
      val sect = part.split(",")
      logger.debug(s"sect=$sect")
      Output(OutputType(sect.head), sect.last.toInt)
    }.toVector
  }
}
