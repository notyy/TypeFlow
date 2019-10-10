package com.notyy.typeflow.domain

trait Function extends Element {
  def inputs: Vector[InputType]

  def inputPattern: InputPattern

  def outputs: Vector[OutputType]

  def outputPattern: OutputPattern
}

case class PureFunction(label: String,
                        inputs: Vector[InputType], inputPattern: InputPattern,
                        outputs: Vector[OutputTypeValue], outputPattern: OutputPattern) extends Element

case class InputType(typeName: String)

trait OutputType
trait OutputTypeValue extends OutputType {
  def typeName: String
}
case class OutputTypeGoodValue(typeName: String) extends OutputTypeValue
case class OutputTypeBadValue(typeName: String) extends OutputTypeValue

case class OutputTypeSideEffect(typeName: String) extends OutputType
case class OutputTypeException(typeName: String) extends OutputType
