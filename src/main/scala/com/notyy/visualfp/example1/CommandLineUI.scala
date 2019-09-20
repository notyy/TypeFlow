package com.notyy.visualfp.example1
import com.notyy.visualfp.example1.UserInputIntepreter.{QuitCommand, UnknownCommand}

import scala.io.StdIn

object CommandLineUI extends App {
  val welcomeStr =
    """
      |welcome to use command line user interface for TFO.
      |just send command to the app
      |:q to quit
      |""".stripMargin

  def askForCommand(): Unit = {
    print(" >")
    val input  = UserInputEndpoint.execute()
    CommandRecorder.execute(input)
    val command = UserInputIntepreter.execute(input)
    val output = command match {
      case UnknownCommand(_) => WrapOutput.execute(command)
      case QuitCommand => WrapOutput.execute(command)
    }
    val resp = UserOutputEndpoint.execute(output)
    if(resp == "quit") System.exit(0)
    askForCommand()
  }

  println(welcomeStr)
  askForCommand()
}
