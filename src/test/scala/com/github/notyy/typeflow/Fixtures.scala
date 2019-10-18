package com.github.notyy.typeflow

import com.github.notyy.typeflow.domain._

object Fixtures {
  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("UserInput"))
  val userInputInterpreter: domain.PureFunction = domain.PureFunction("UserInputInterpreter", inputs = Vector(Input(InputType("UserInput"),1)),
    outputs = Vector(
      Output(OutputType("UnknownCommand"), 1),
      Output(OutputType("QuitCommand"), 2),
      Output(OutputType("CreateModelCommand"),3),
      Output(OutputType("AddInputEndpointCommand"),4),
      Output(OutputType("AddFunctionCommand"),5),
      Output(OutputType("AddOutputEndpointCommand"),6),
      Output(OutputType("CreateFlowCommand"),7),
      Output(OutputType("AddInstanceCommand"),8),
      Output(OutputType("ConnectElementCommand"),9)
    ))
  val wrapOutput: domain.PureFunction = domain.PureFunction("WrapOutput", inputs = Vector(Input(InputType("java.lang.Object"),1)),
    outputs = Vector(Output(OutputType("WrappedOutput"), 1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", Vector(Input(InputType("WrappedOutput"),1)), OutputType("Unit"), Vector.empty)
  val createNewModel: OutputEndpoint = OutputEndpoint("CreateNewModel",Vector(Input(InputType("CreateModelCommand"),1)), OutputType("ModelCreationSuccess"), Vector.empty)
  val addDefinition: PureFunction = PureFunction("AddDefinition",inputs = Vector(Input(InputType("Model"),1), Input(InputType("AddDefinitionCommand"),2)),
    outputs = Vector(Output(OutputType("Model"),1))
  )
  val model2Json: PureFunction = PureFunction("Model2Json",Vector(Input(InputType("Model"),1)),Vector(Output(OutputType("java.lang.String"),1)))
  val getModelPath: PureFunction = PureFunction("GetModelPath",Vector(Input(InputType("Model"),1)), Vector(Output(OutputType("Path"),1)))
  val saveToFile: OutputEndpoint = OutputEndpoint("SaveToFile",
    inputs = Vector(
      Input(InputType("Path"),1),Input(InputType("java.lang.String"),2)
    ),
    outputType = OutputType("Unit"),
    errorOutputs = Vector(Output(OutputType("java.lang.String"),1))
  )
  val onSaveModelSuccess: PureFunction = PureFunction("OnSaveModelSuccess", Vector(Input(InputType("Model"),1),Input(InputType("Unit"),2)),Vector(Output(OutputType("ModelUpdateSuccess"),1)))

  val minimalFlow: Flow = Flow("minimalFlow",
    instances = Vector(
      //use definition name as default instance id
      Instance(userInputEndpoint),
      Instance(userInputInterpreter),
      Instance(wrapOutput),
      Instance(outputEndpoint),
      Instance(createNewModel),
      Instance(addDefinition),
      Instance(model2Json),
      Instance(getModelPath),
      Instance(saveToFile),
      Instance(onSaveModelSuccess)
    ),
    connections = Vector(
      Connection(userInputEndpoint.name, 1, userInputInterpreter.name),
      Connection(userInputInterpreter.name, 1, wrapOutput.name),
      Connection(userInputInterpreter.name, 2, wrapOutput.name),

      Connection(userInputInterpreter.name, 3, createNewModel.name),
      Connection(createNewModel.name, 1, wrapOutput.name),

      Connection(userInputInterpreter.name, 4, addDefinition.name),
      Connection(addDefinition.name, 1, model2Json.name),
      Connection(addDefinition.name, 1, getModelPath.name),
      Connection(getModelPath.name,1,saveToFile.name),
      Connection(model2Json.name,1,saveToFile.name),
      Connection(addDefinition.name,1,onSaveModelSuccess.name),
      Connection(saveToFile.name,1,onSaveModelSuccess.name),
      Connection(onSaveModelSuccess.name,1,wrapOutput.name),
      Connection(wrapOutput.name, 1, outputEndpoint.name),
    )
  )
  val model: Model = domain.Model("typeflow_editor", Vector(userInputEndpoint, userInputInterpreter, wrapOutput, outputEndpoint), Vector(minimalFlow), Some(minimalFlow))
}
