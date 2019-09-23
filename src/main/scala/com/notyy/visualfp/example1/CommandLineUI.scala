package com.notyy.visualfp.example1
import com.notyy.visualfp.example1.UserInputIntepreter.{AddElementCommand, ConnectElementCommand, CreateModelCommand, QuitCommand, UnknownCommand}

import scala.io.StdIn
import scala.util.{Failure, Success}

object CommandLineUI extends App {
  val welcomeStr =
    """
      |welcome to use command line user interface for TFO.
      |just send command to the app
      |:q to quit
      |""".stripMargin

  @scala.annotation.tailrec
  def askForCommand(): Unit = {
    print(" >")
    //TODO this block of code is actually used as flow engine. it should be externalized later.
    val input  = UserInputEndpoint.execute()
    CommandRecorder.execute(input)
    val command = UserInputIntepreter.execute(input)
    val output = command match {
      case UnknownCommand(_) => WrapOutput.execute(command)
      case QuitCommand => WrapOutput.execute(command)
      case createModelCommand: CreateModelCommand => {
        val unsavedModel = CreateModel.execute(createModelCommand)
        val saveRs = SaveNewModel.execute(unsavedModel)
        WrapOutput.execute(saveRs)
      }
      case addElementCommand: AddElementCommand => {
        val savedModel = ReadModel.execute(addElementCommand.modelName)
        val modifiedModel = AddModelElement.execute(savedModel,addElementCommand)
        val updateRs = UpdateModel.execute(modifiedModel)
        WrapOutput.execute(updateRs)
      }
      case connectElementCommand: ConnectElementCommand => {
        val savedModel = ReadModel.execute(connectElementCommand.modelName)
        val modifiedModel = ConnectModelElement.execute(savedModel, connectElementCommand)
        val updateRs = UpdateModel.execute(modifiedModel)
        WrapOutput.execute(updateRs)
      }
    }
    val resp = UserOutputEndpoint.execute(output)
    if(resp == "quit") System.exit(0)
    askForCommand()
  }

  println(welcomeStr)
  askForCommand()
}
