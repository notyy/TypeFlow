package com.notyy.visualfp.domain

trait InputEndpoint extends Function

case class HttpInputEndpoint(label: String,
                             inputs: Vector[InputType], inputPattern: InputPattern,
                             outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint
case class DBInputEndpoint(label: String,
                           inputs: Vector[InputType], inputPattern: InputPattern,
                           outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint
case class ConstantInputEndpoint(label: String, output: OutputType) extends InputEndpoint {
  override def inputs: Vector[InputType] = Vector.empty

  override def inputPattern: InputPattern = NoInput

  override def outputs: Vector[OutputType] = Vector(output)

  override def outputPattern: OutputPattern = OneOfOutput
}

//case class CommandLineInputEndpoint()

trait OutputEndpoint extends Element

case class HttpOutputEndpoint(label: String,
                              inputs: Vector[InputType], inputPattern: InputPattern,
                              outputs: Vector[OutputType], outputPattern: OutputPattern) extends OutputEndpoint

case class DBOutputEndPoint(label: String,
                            inputs: Vector[InputType], inputPattern: InputPattern,
                            outputs: Vector[OutputType], outputPattern: OutputPattern) extends Element
