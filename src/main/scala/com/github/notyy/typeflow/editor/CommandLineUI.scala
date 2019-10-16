package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import com.typesafe.scalalogging.Logger

object CommandLineUI extends App {
  private val logger = Logger("CommandLineUI")
  val welcomeStr =
    """
      |welcome to use command line user interface for TFO.
      |just send command to the app
      |:q to quit
      |""".stripMargin

  val json = ReadFileFromResource.execute("/TypeFlowEditor.typeflow")
  val model: Model = Json2Model.execute(json)

  println(welcomeStr)
  askForCommand()

  @scala.annotation.tailrec
  def askForCommand(): Unit = {
    print(" >")
    val input = UserInputEndpoint.execute()
    val instance = model.activeFlow.instances.find(_.id == "UserInputEndpoint").get
    val localRunEngine = LocalRunEngine(model, Some("com.github.notyy.typeflow.editor"))
    localRunEngine.startFlow(input, instance)
    askForCommand()
  }

  def onResponse(resp: String): Unit = {
    println(resp)
    if (resp == "quit") System.exit(0)
  }
}
