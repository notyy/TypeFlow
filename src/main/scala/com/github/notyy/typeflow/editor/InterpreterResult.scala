package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Input, InputType, Output, OutputType}

sealed trait InterpreterResult
case class UnknownCommand(input: String) extends InterpreterResult
case object QuitCommand extends InterpreterResult
case class CreateModelCommand(modelName: String) extends InterpreterResult

sealed trait AddDefinitionCommand extends InterpreterResult
case class AddInputEndpointCommand(modelName: String, name: String, outputType: OutputType) extends AddDefinitionCommand
case class AddFunctionCommand(modelName: String, name: String, inputs: Vector[Input], outputs: Vector[Output]) extends AddDefinitionCommand
case class AddOutputEndpointCommand(modelName: String, name: String, inputType: InputType, outputType: OutputType, errorOutput: Vector[Output]) extends AddDefinitionCommand

sealed trait FlowOperationCommand extends InterpreterResult
case class CreateFlowCommand(modelName: String, name: String) extends FlowOperationCommand
case class AddInstanceCommand(modelName: String, flowName: String,definitionName: String) extends FlowOperationCommand
case class ConnectInstanceCommand(fromInstanceId: String, outputIndex: Int, toInstanceId: String, modelName: String,flowName: String) extends FlowOperationCommand