package com.github.notyy.typeflow

import com.github.notyy.typeflow.domain._

object Fixtures {
  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("UIE::UserInput"))
  val userInputInterpreter: domain.PureFunction = domain.PureFunction("UserInputInterpreter", inputs = Vector(Input(InputType("UserInput"), 1)),
    outputs = Vector(
      Output(OutputType("UII::UnknownCommand"), 1),
      Output(OutputType("UII::QuitCommand"), 2),
      Output(OutputType("UII::CreateModelCommand"), 3),
      Output(OutputType("UII::AddInputEndpointCommand"), 4),
      Output(OutputType("UII::AddFunctionCommand"), 5),
      Output(OutputType("UII::AddOutputEndpointCommand"), 6),
      Output(OutputType("UII::CreateFlowCommand"), 7),
      Output(OutputType("UII::AddInstanceCommand"), 8),
      Output(OutputType("UII::ConnectElementCommand"), 9)
    ))
  val command2ModelName: PureFunction = PureFunction("Command2ModelName", Vector(Input(InputType("Model"), 1)), Vector(Output(OutputType("C2MN::String"), 1)))
  val wrapOutput: domain.PureFunction = domain.PureFunction("WrapOutput", inputs = Vector(Input(InputType("Object"), 1)),
    outputs = Vector(Output(OutputType("WO::WrappedOutput"), 1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", Vector(Input(InputType("CLOE::WrappedOutput"), 1)), OutputType("CLOE::Unit"), Vector.empty)
  val createNewModel: OutputEndpoint = OutputEndpoint("CreateNewModel", Vector(Input(InputType("CreateModelCommand"), 1)), OutputType("CNM::ModelCreationSuccess"), Vector.empty)
  val addDefinition: PureFunction = PureFunction("AddDefinition", inputs = Vector(Input(InputType("Model"), 1), Input(InputType("AD::AddDefinitionCommand"), 2)),
    outputs = Vector(Output(OutputType("Model"), 1))
  )
  val model2Json: PureFunction = PureFunction("Model2Json", Vector(Input(InputType("Model"), 1)), Vector(Output(OutputType("M2J::String"), 1)))
  val getModelPath: PureFunction = PureFunction("GetModelPath", Vector(Input(InputType("java.lang.String"), 1)), Vector(Output(OutputType("GMP::Path"), 1)))
  val readFile: OutputEndpoint = OutputEndpoint("ReadFile", Vector(Input(InputType("Path"), 1)), OutputType("RF::String"), Vector.empty)
  val json2Model: PureFunction = PureFunction("Json2Model",Vector(Input(InputType("java.lang.String"),1)),Vector(Output(OutputType("J2M::Model"),1)))
  val saveToFile: OutputEndpoint = OutputEndpoint("SaveToFile",
    inputs = Vector(
      Input(InputType("Path"), 1), Input(InputType("java.lang.String"), 2)
    ),
    outputType = OutputType("STF::Unit"),
    errorOutputs = Vector(Output(OutputType("STF::String"), 1))
  )
  val onSaveModelSuccess: PureFunction = PureFunction("OnSaveModelSuccess", Vector(Input(InputType("Model"), 1), Input(InputType("Unit"), 2)), Vector(Output(OutputType("OSMS::ModelUpdateSuccess"), 1)))

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
      Instance(onSaveModelSuccess),
      Instance(command2ModelName),
      Instance(readFile),
      Instance(json2Model),
    ),
    connections = Vector(
      Connection(userInputEndpoint.name, 1, userInputInterpreter.name),
      Connection(userInputInterpreter.name, 1, wrapOutput.name),
      Connection(userInputInterpreter.name, 2, wrapOutput.name),

      Connection(userInputInterpreter.name, 3, createNewModel.name),
      Connection(createNewModel.name, 1, wrapOutput.name),

      Connection(userInputInterpreter.name, 4, addDefinition.name),
      Connection(userInputInterpreter.name, 4, command2ModelName.name),
      Connection(command2ModelName.name, 1, readFile.name),
      Connection(readFile.name, 1, json2Model.name),
      Connection(json2Model.name, 1, addDefinition.name),

      Connection(addDefinition.name, 1, model2Json.name),
      Connection(addDefinition.name, 1, getModelPath.name),
      Connection(getModelPath.name, 1, saveToFile.name),
      Connection(model2Json.name, 1, saveToFile.name),
      Connection(addDefinition.name, 1, onSaveModelSuccess.name),
      Connection(saveToFile.name, 1, onSaveModelSuccess.name),
      Connection(onSaveModelSuccess.name, 1, wrapOutput.name),
      Connection(wrapOutput.name, 1, outputEndpoint.name),
    )
  )
  val model: Model = domain.Model("typeflow_editor", Vector(userInputEndpoint, userInputInterpreter, wrapOutput, outputEndpoint), Vector(minimalFlow), Some(minimalFlow))
}
