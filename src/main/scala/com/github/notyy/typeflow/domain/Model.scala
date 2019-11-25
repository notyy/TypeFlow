package com.github.notyy.typeflow.domain

case class Model(name: String, definitions: Vector[Definition], flows: Vector[Flow], activeFlow: Option[Flow])

case class Element(elementType: String, name: String)

//TODO considering make every Definition have inputs and outputs, just some are empty
trait Definition {
  def name: String
  def inputs: Vector[Input]
  def outputs: Vector[Output]
}

trait InputEndpoint extends Definition {
  def subName: String
  def outputType: OutputType
  override def inputs: Vector[Input] = Vector.empty

  override def outputs: Vector[Output] = Vector(Output(outputType,1))
}
case class CommandLineInputEndpoint(name: String, outputType: OutputType) extends InputEndpoint {
  override def subName: String = "CommandLineInputEndpoint"
}
case class FileInputEndpoint(name: String, outputType: OutputType) extends InputEndpoint {
  override def subName: String = "FileInputEndpoint"
}
case class CommandLineArgsInputEndpoint(name: String, outputType: OutputType) extends InputEndpoint {
  override def subName: String = "CommandLineArgsInputEndpoint"
}
case class AliyunHttpInputEndpoint(name: String, outputType: OutputType) extends InputEndpoint {
  override def subName: String = "AliyunHttpInputEndpoint"
}

case class PureFunction(name: String, inputs: Vector[Input], outputs: Vector[Output]) extends Definition

case class OutputEndpoint(name: String, inputs: Vector[Input], outputType: OutputType, errorOutputs: Vector[Output]) extends Definition {
  override def outputs: Vector[Output] = Vector(Output(outputType,1))
}
case class FileOutputEndpoint(name: String, inputs: Vector[Input], outputType: OutputType, errorOutputs: Vector[Output]) extends Definition {
  override def outputs: Vector[Output] = Vector(Output(outputType,1))
}
case class AliyunOSSOutputEndpoint(name: String, inputs: Vector[Input], outputType: OutputType, errorOutputs: Vector[Output]) extends Definition {
  override def outputs: Vector[Output] = Vector(Output(outputType,1))
}

case class Input(inputType: InputType, index: Int)

case class Output(outputType: OutputType, index: Int)

case class OutputType(name: String)

case class InputType(name: String)

case class Flow(name: String, instances: Vector[Instance], connections: Vector[Connection])

//there are sometimes that one definition have multiple instance in the flow
case class Instance(id: String, definition: Definition)

object Instance {
  def apply(definition: Definition): Instance = Instance(definition.name, definition)
}

case class Connection(fromInstanceId: String, outputIndex: Int, toInstanceId: String, inputIndex: Int)
