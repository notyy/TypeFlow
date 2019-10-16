package com.github.notyy.typeflow.domain

case class Model(name: String, definitions: Vector[Definition], flows: Vector[Flow], activeFlow: Option[Flow])

case class Element(elementType: String, name: String)

trait Definition {
  def name: String
}

case class InputEndpoint(name: String, outputType: OutputType) extends Definition

case class Function(name: String, inputs: Vector[Input], outputs: Vector[Output]) extends Definition

case class OutputEndpoint(name: String, inputType: InputType, outputType: OutputType, errorOutputs: Vector[Output]) extends Definition

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

case class Connection(fromInstanceId: String, outputIndex: Int, toInstanceId: String)
