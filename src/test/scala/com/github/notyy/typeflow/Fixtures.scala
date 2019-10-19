package com.github.notyy.typeflow

import com.github.notyy.typeflow.domain._

object Fixtures {
  val mp = "com.github.notyy.typeflow.domain"

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
  val command2ModelName: PureFunction = PureFunction("Command2ModelName", Vector(Input(InputType(s"AddDefinitionCommand"), 1)), Vector(Output(OutputType("C2MN::String"), 1)))
  val wrapOutput: domain.PureFunction = domain.PureFunction("WrapOutput", inputs = Vector(Input(InputType("Object"), 1)),
    outputs = Vector(Output(OutputType("WO::WrappedOutput"), 1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("CommandLineOutputEndpoint", Vector(Input(InputType("CLOE::WrappedOutput"), 1)), OutputType("CLOE::Unit"), Vector.empty)
  val createNewModel: OutputEndpoint = OutputEndpoint("CreateNewModel", Vector(Input(InputType("CreateModelCommand"), 1)), OutputType(s"CNM::$mp.ModelCreationSuccess"), Vector.empty)
  val addDefinition: PureFunction = PureFunction("AddDefinition", inputs = Vector(Input(InputType(s"$mp.Model"), 1), Input(InputType("AD::AddDefinitionCommand"), 2)),
    outputs = Vector(Output(OutputType(s"$mp.Model"), 1))
  )
  val model2Json: PureFunction = PureFunction("Model2Json", Vector(Input(InputType(s"$mp.Model"), 1)), Vector(Output(OutputType("M2J::String"), 1)))
  val getModelPath: PureFunction = PureFunction("GetModelPath", Vector(Input(InputType("java.lang.String"), 1)), Vector(Output(OutputType("GMP::Path"), 1)))
  val readFile: OutputEndpoint = OutputEndpoint("ReadFile", Vector(Input(InputType("Path"), 1)), OutputType("RF::String"), Vector.empty)
  val json2Model: PureFunction = PureFunction("Json2Model",Vector(Input(InputType("java.lang.String"),1)),Vector(Output(OutputType(s"J2M::$mp.Model"),1)))
  val saveToFile: OutputEndpoint = OutputEndpoint("SaveToFile",
    inputs = Vector(
      Input(InputType("Path"), 1), Input(InputType("java.lang.String"), 2)
    ),
    outputType = OutputType("STF::Unit"),
    errorOutputs = Vector(Output(OutputType("STF::String"), 1))
  )
  val onSaveModelSuccess: PureFunction = PureFunction("OnSaveModelSuccess", Vector(Input(InputType(s"$mp.Model"), 1), Input(InputType("Unit"), 2)), Vector(Output(OutputType(s"OSMS::$mp.ModelUpdateSuccess"), 1)))

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
      Instance("getModelPath1",getModelPath),
      Instance(saveToFile),
      Instance(onSaveModelSuccess),
      Instance(command2ModelName),
      Instance(readFile),
      Instance(json2Model),
    ),
    connections = Vector(
      Connection(userInputEndpoint.name, 1, userInputInterpreter.name,1),
      Connection(userInputInterpreter.name, 1, wrapOutput.name,1),
      Connection(userInputInterpreter.name, 2, wrapOutput.name,1),

      Connection(userInputInterpreter.name, 3, createNewModel.name,1),
      Connection(createNewModel.name, 1, wrapOutput.name,1),

      Connection(userInputInterpreter.name, 4, addDefinition.name,2),
      Connection(userInputInterpreter.name, 4, command2ModelName.name,1),
      Connection(command2ModelName.name, 1, "getModelPath1",1),
      Connection("getModelPath1", 1, readFile.name,1),
      Connection(readFile.name, 1, json2Model.name,1),
      Connection(json2Model.name, 1, addDefinition.name,1),

      Connection(addDefinition.name, 1, model2Json.name,1),
      Connection(addDefinition.name, 1, getModelPath.name,1),
      Connection(getModelPath.name, 1, saveToFile.name,1),
      Connection(model2Json.name, 1, saveToFile.name,1),
      Connection(addDefinition.name, 1, onSaveModelSuccess.name,1),
      Connection(saveToFile.name, 1, onSaveModelSuccess.name,1),
      Connection(onSaveModelSuccess.name, 1, wrapOutput.name,1),
      Connection(wrapOutput.name, 1, outputEndpoint.name,1),
    )
  )
  val model: Model = domain.Model("typeflow_editor", Vector(userInputEndpoint, userInputInterpreter, wrapOutput, outputEndpoint), Vector(minimalFlow), Some(minimalFlow))

  //define an multi parameter simple model
  val numInput: InputEndpoint = InputEndpoint("NumInput",OutputType("NI::Integer"))
  val add2: PureFunction = PureFunction("Add2",Vector(Input(InputType("Integer"),1)),Vector(Output(OutputType("A2::Integer"),1)))
  val multi3: PureFunction = PureFunction("Multi3", Vector(Input(InputType("Integer"),1)),Vector(Output(OutputType("M3::Integer"),1)))
  val addAndPrint: OutputEndpoint = OutputEndpoint("AddAndPrint",
    inputs = Vector(
      Input(InputType("Integer"),1),
      Input(InputType("Integer"),2),
    ),
    outputType = OutputType("AAP::Integer"),
    errorOutputs = Vector.empty
  )
  val definitions: Vector[Definition] = Vector(numInput,add2,multi3,addAndPrint)

  val flow = Flow("flow",
    instances = Vector(
      Instance(numInput),
      Instance(add2),
      Instance(multi3),
      Instance(addAndPrint)
    ),
    connections = Vector(
      Connection(numInput.name,1,add2.name,1),
      Connection(numInput.name,1,multi3.name,1),
      Connection(add2.name,1,addAndPrint.name,1),
      Connection(multi3.name,1,addAndPrint.name,2),
    )
  )
  val multiParamModel: Model = Model("testModel",definitions,Vector(flow),Some(flow))
}
