package com.notyy.visualfp.domain

trait InputEndpoint extends Function

case class HttpInputEndpoint(label: Option[String],
                             inputs: Vector[InputType], inputPattern: InputPattern,
                             outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint
case class DBInputEndpoint(label: Option[String],
                           inputs: Vector[InputType], inputPattern: InputPattern,
                           outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint
case class ConstantInputEndpoint(label: Option[String],output: OutputType) extends InputEndpoint {
  override def inputs: Vector[InputType] = Vector.empty

  override def inputPattern: InputPattern = NoInput

  override def outputs: Vector[OutputType] = Vector(output)

  override def outputPattern: OutputPattern = OneOfOutput
}

trait OutputEndpoint extends Element

case class HttpOutputEndpoint(label: Option[String],
                              inputs: Vector[InputType], inputPattern: InputPattern,
                              outputs: Vector[OutputType], outputPattern: OutputPattern) extends OutputEndpoint

case class DBOutputEndPoint(label: Option[String],
                            inputs: Vector[InputType], inputPattern: InputPattern,
                            outputs: Vector[OutputType], outputPattern: OutputPattern) extends Element
