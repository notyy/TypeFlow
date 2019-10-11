package com.github.notyy.typeflow.example1

case class Model(name: String, definitions: Vector[Definition], flows: Vector[Flow], activeFlow: Flow)
case class Element(elementType: String, name: String)

trait Definition{
  def name: String
}
case class InputEndpoint(name: String, output: OutputType) extends Definition
case class Function(name: String, input: InputType, outputs: Vector[Output]) extends Definition
case class OutputEndpoint(name: String, input: InputType, output: OutputType, errorOutput: Vector[Output]) extends Definition

case class Output(outputType: OutputType, index: Int)
case class OutputType(name: String)
case class InputType(name: String)

case class Flow(name: String, instances: Vector[Instance], connections: Vector[Connection])

//there are sometimes that one definition have multiple instance in the flow
case class Instance(id: String, definition: Definition)
object Instance {
  def apply(definition: Definition): Instance = Instance(definition.name, definition)
}
case class Connection(fromInstanceId: String,outputIndex: Int, toInstanceId: String)
