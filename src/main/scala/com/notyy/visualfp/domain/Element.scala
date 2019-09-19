package com.notyy.visualfp.domain

trait Element {
  def label: Option[String]
}

trait InputUserInterface extends Element {
  def output: OutputType
}
case class WebInputUserInterface(label: Option[String], output: OutputType) extends InputUserInterface

trait OutputUserInterface extends Element {
  def input: InputType
}
case class WebOutputUserInterface(label: Option[String], input: InputType) extends OutputUserInterface

case class DBTable(label: Option[String], inputType: InputType, outputType: OutputType)

trait Function extends Element {
  def inputs: Vector[InputType]

  def inputPattern: InputPattern

  def outputs: Vector[OutputType]

  def outputPattern: OutputPattern
}

case class PureFunction(label: Option[String],
                        inputs: Vector[InputType], inputPattern: InputPattern,
                        outputs: Vector[OutputTypeValue], outputPattern: OutputPattern) extends Element

trait InputPattern

case object OneOfInput extends InputPattern

case object AllInput extends InputPattern

trait OutputPattern

case object OneOfOutput extends OutputPattern

case object AllOutput extends OutputPattern

case class InputType(typeName: String)

trait OutputType
trait OutputTypeValue extends OutputType {
  def typeName: String
}
case class OutputTypeGoodValue(typeName: String) extends OutputTypeValue
case class OutputTypeBadValue(typeName: String) extends OutputTypeValue

case class OutputTypeSideEffect(typeName: String) extends OutputType
case class OutputTypeException(typeName: String) extends OutputType

trait InputEndpoint extends Function

case class HttpInputEndpoint(label: Option[String],
                             inputs: Vector[InputType], inputPattern: InputPattern,
                             outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint
case class DBInputEndpoint(label: Option[String],
                             inputs: Vector[InputType], inputPattern: InputPattern,
                             outputs: Vector[OutputType], outputPattern: OutputPattern) extends InputEndpoint

trait OutputEndpoint extends Element

case class HttpOutputEndpoint(label: Option[String],
                              inputs: Vector[InputType], inputPattern: InputPattern,
                              outputs: Vector[OutputType], outputPattern: OutputPattern) extends OutputEndpoint

case class DBOutputEndPoint(label: Option[String],
                            inputs: Vector[InputType], inputPattern: InputPattern,
                            outputs: Vector[OutputType], outputPattern: OutputPattern) extends Element

case class Connection(output: OutputType, input: InputType)
