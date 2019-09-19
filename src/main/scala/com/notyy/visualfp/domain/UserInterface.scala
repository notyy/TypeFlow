package com.notyy.visualfp.domain

trait InputUserInterface extends Element {
  def output: OutputType
}
case class WebInputUserInterface(label: Option[String], output: OutputType) extends InputUserInterface

trait OutputUserInterface extends Element {
  def input: InputType
}
case class WebOutputUserInterface(label: Option[String], input: InputType) extends OutputUserInterface

case class DBTable(label: Option[String], inputType: InputType, outputType: OutputType)

case class Connection(output: OutputType, input: InputType)
