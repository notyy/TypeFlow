package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.OutputType

sealed trait InterpreterResult
case class UnknownCommand(input: String) extends InterpreterResult
case object QuitCommand extends InterpreterResult
case class CreateModelCommand(modelName: String) extends InterpreterResult

sealed trait ModifyModelCommand extends InterpreterResult
case class AddInputEndpointCommand(modelName: String, name: String, outputType: OutputType) extends ModifyModelCommand
case class ConnectInstanceCommand(fromInstanceId: String, outputIndex: Int, toInstanceId: String, modelName: String,flowName: String) extends ModifyModelCommand