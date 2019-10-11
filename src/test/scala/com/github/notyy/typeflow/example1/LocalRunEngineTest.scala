package com.github.notyy.typeflow.example1

import com.github.notyy.typeflow.example1.UserInputInterpreter.QuitCommand
import org.scalatest.{FunSpec, Matchers}

class LocalRunEngineTest extends FunSpec with Matchers {

  val userInputEndpoint: InputEndpoint = InputEndpoint("UserInputEndpoint", OutputType("String"))
  val userInputInterpreter: Function = Function("UserInputInterpreter", InputType("String"),
    outputs = Vector(
      Output(OutputType("UnknownCommand"), 1),
      Output(OutputType("QuitCommand"), 2)
    ))
  val wrapOutput: Function = Function("WrapOutput", InputType("java.lang.Object"),
    outputs = Vector(Output(OutputType("String"),1))
  )
  val outputEndpoint: OutputEndpoint = OutputEndpoint("UserOutputEndpoint",InputType("String"),OutputType("String"),Vector.empty)
  val minimalFlow: Flow = Flow("minimalFlow",
    instances = Vector(
      //use definition name as default instance id
      Instance(userInputEndpoint),
      Instance(userInputInterpreter),
      Instance(wrapOutput)
    ),
    connections = Vector(
      Connection(userInputEndpoint.name,1,userInputInterpreter.name),
      Connection(userInputInterpreter.name,1, wrapOutput.name),
      Connection(userInputInterpreter.name,2, wrapOutput.name),
      Connection(wrapOutput.name,1,outputEndpoint.name)
    )
  )
  val model: Model = Model("typeflow_editor",Vector(userInputEndpoint,userInputInterpreter,wrapOutput,outputEndpoint),Vector(minimalFlow),minimalFlow)

  describe("LocalRunEngine"){
    it("can find next instance by the type of input"){
      val x:Any = QuitCommand
      val localRunEngine = LocalRunEngine(model,userInputEndpoint.name,Some("com.github.notyy.typeflow.example1"))
      localRunEngine.nextInstances(":q",Instance(userInputEndpoint)) shouldBe Vector(Instance(userInputInterpreter))
      localRunEngine.nextInstances(QuitCommand,Instance(userInputInterpreter)) shouldBe Vector(Instance(wrapOutput))
    }
  }
}
