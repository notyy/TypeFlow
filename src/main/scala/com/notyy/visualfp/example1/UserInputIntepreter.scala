package com.notyy.visualfp.example1

object UserInputIntepreter {
  trait Result
  case class UnknownCommand(input: String) extends Result
  case object QuitCommand extends Result
  case class CreateModelCommand(modelName: String) extends Result
  case class ModifyModelCommand(command: String) extends Result

  private val MarkPattern = """(.*) (.*)""".r

  def execute(input: String): Result = {
    input match {
      case ":q" => QuitCommand
      case (MarkPattern("CreateModel",modelName)) => CreateModelCommand(modelName)
      case _ => UnknownCommand(input)
    }
  }
}
