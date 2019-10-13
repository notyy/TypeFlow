package com.github.notyy.typeflow.example1

import com.typesafe.scalalogging.Logger

object CommandLineUI extends App {
  private val logger = Logger("CommandLineUI")
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
    val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("java.lang.String"))
    val userInputInterpreter: Function = Function("UserInputInterpreter", InputType("java.lang.String"),
      outputs = Vector(
        Output(OutputType("UnknownCommand"), 1),
        Output(OutputType("QuitCommand"), 2)
      ))
    val wrapOutput: Function = Function("WrapOutput", InputType("java.lang.Object"),
      outputs = Vector(Output(OutputType("java.lang.String"), 1))
    )
    val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", InputType("java.lang.String"), OutputType("Unit"), Vector.empty)
    val minimalFlow: Flow = Flow("minimalFlow",
      instances = Vector(
        //use definition name as default instance id
        Instance(userInputEndpoint),
        Instance(userInputInterpreter),
        Instance(wrapOutput),
        Instance(outputEndpoint)
      ),
      connections = Vector(
        Connection(userInputEndpoint.name, 1, userInputInterpreter.name),
        Connection(userInputInterpreter.name, 1, wrapOutput.name),
        Connection(userInputInterpreter.name, 2, wrapOutput.name),
        Connection(wrapOutput.name, 1, outputEndpoint.name)
      )
    )
    val model: Model = Model("typeflow_editor", Vector(userInputEndpoint, userInputInterpreter, wrapOutput, outputEndpoint), Vector(minimalFlow), minimalFlow)
    val input = UserInputEndpoint.execute()
    val instance = model.activeFlow.instances.find(_.id == userInputEndpoint.name).get
    val localRunEngine = LocalRunEngine(model, Some("com.github.notyy.typeflow.example1"))
    localRunEngine.startFlow(input, instance)
    //    CommandRecorder.execute(input)
    //    val command = UserInputInterpreter.execute(input)
    //    val output = command match {
    //      case UnknownCommand(_) => WrapOutput.execute(command)
    //      case QuitCommand => WrapOutput.execute(command)
    //      case createModelCommand: CreateModelCommand => {
    //        val unsavedModel = CreateModel.execute(createModelCommand)
    //        val saveRs = SaveNewModel.execute(unsavedModel)
    //        WrapOutput.execute(saveRs)
    //      }
    //      case addElementCommand: AddInputEndpointCommand => {
    //        val savedModel = ReadModel.execute(addElementCommand.modelName)
    //        val modifiedModel = AddDefinition.execute(savedModel, addElementCommand)
    //        val updateRs = UpdateModel.execute(modifiedModel)
    //        WrapOutput.execute(updateRs)
    //      }
    //      case connectElementCommand: ConnectInstanceCommand => {
    //        val savedModel = ReadModel.execute(connectElementCommand.modelName)
    //        val modifiedModel = ConnectModelElement.execute(savedModel, connectElementCommand)
    //        val updateRs = UpdateModel.execute(modifiedModel)
    //        WrapOutput.execute(updateRs)
    //      }
    //    }
    //    val resp = UserOutputEndpoint.execute(output)
    //    if (resp == "quit") System.exit(0)
    askForCommand()
  }

  def onResponse(resp: String): Unit = {
    println(resp)
    if (resp == "quit") System.exit(0)
  }

  println(welcomeStr)
  askForCommand()
}
