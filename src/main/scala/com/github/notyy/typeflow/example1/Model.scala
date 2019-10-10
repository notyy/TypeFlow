package com.github.notyy.typeflow.example1

case class Model(name: String, definitions: Vector[Definition], flows: Vector[Flow], activeFlow: Flow)
case class Element(elementType: String, name: String)

trait Definition
case class InputEndpoint(name: String, output: OutputType) extends Definition
case class Function(name: String, input: InputType, output: Vector[Output]) extends Definition
case class OutputEndpoint(name: String, output: Vector[Output], errorOutput: Vector[Output]) extends Definition

case class Output(outputType: OutputType, index: Int)
case class OutputType(name: String)
case class InputType(name: String)

case class Flow(name: String, instances: Vector[Instance], connections: Vector[Connection])
case class Instance(id: String, definition: Definition)
case class Connection(fromInstanceId: String,outputIndex: Int, toInstanceId: String)
