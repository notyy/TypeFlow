package com.notyy.visualfp.example1

object UserInputIntepreter {
  trait Result
  case class UnknownCommand(input: String) extends Result
  case object QuitCommand extends Result
  case class CreateModelCommand(modelName: String) extends Result

  sealed trait ModifyModelCommand extends Result
  case class AddElementCommand(elementType: String, elementName: String, modelName: String) extends Result
  case class ConnectElementCommand(from: String, to: String, modelName: String) extends Result

  private val CreateModelPattern = """(.*) (.*)""".r
  private val AddElementPattern = """Add (.*) (.*) in (.*)""".r
  private val ConnectElementPattern = """Connect from (.*) to (.*) in (.*)""".r

  def execute(input: String): Result = {
    input match {
      case ":q" => QuitCommand
      case (CreateModelPattern("CreateModel", modelName)) => CreateModelCommand(modelName)
      case (AddElementPattern(elementType, elementName, modelName)) => AddElementCommand(elementType, elementName, modelName)
      case (ConnectElementPattern(from, to, modelName)) => ConnectElementCommand(from, to, modelName)
      case _ => UnknownCommand(input)
    }
  }
}
