package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.{Fixtures, domain}
import com.github.notyy.typeflow.domain._
import com.github.notyy.typeflow.editor.{InterpreterResult, Path, QuitCommand, UnknownCommand, UserInput, UserInputInterpreter, WrappedOutput}
import org.scalatest.{FunSpec, Matchers}

import scala.util.{Success, Try}

class ReflectRunnerTest extends FunSpec with Matchers {
  val packgePrefix = Some("com.github.notyy.typeflow.editor")
  describe("ReflectRunner") {
    //TODO will fix this for scala
//    it("should run defined function by name") {
//      val userInputInterpreter: domain.PureFunction = domain.PureFunction("UserInputInterpreter", Vector(Input(InputType("UserInput"),1)),
//        outputs = Vector(
//          Output(OutputType("UnknownCommand"), 1),
//          Output(OutputType("QuitCommand"), 2)
//        ))
//      ReflectRunner.run(userInputInterpreter, packgePrefix, Some(Vector(UserInput(":q")))).
//        asInstanceOf[InterpreterResult] shouldBe QuitCommand
//      ReflectRunner.run(userInputInterpreter, packgePrefix, Some(Vector(UserInput("badcommand")))).
//        asInstanceOf[InterpreterResult] shouldBe UnknownCommand("badcommand")
//      ReflectRunner.run(Fixtures.getModelPath,packgePrefix, Some(Vector("newModel"))).
//        asInstanceOf[Path] shouldBe Path("./localoutput/newModel.typeflow")
//    }
//    it("should run InputEndpoint") {
//      val mockInputEndpoint: InputEndpoint = CommandLineInputEndpoint("com.github.notyy.typeflow.util.MockInputEndpoint", OutputType("UserInput"))
//      val rs = ReflectRunner.run(mockInputEndpoint, None, None)
//      println(s"rs=$rs")
//      rs.asInstanceOf[UserInput] shouldBe UserInput("mock input")
//    }
//    it("should run OutputEndpoint") {
//      val mockOutputEndpoint: OutputEndpoint = OutputEndpoint("com.github.notyy.typeflow.util.MockOutputEndpoint",Vector(Input(InputType("com.github.notyy.typeflow.editor.WrappedOutput"),1)), OutputType("Unit"),Vector.empty)
//      ReflectRunner.run(mockOutputEndpoint,None,Some(Vector(WrappedOutput("input")))) shouldBe ()
//    }
  }
}

object MockInputEndpoint {
  def execute(): UserInput = UserInput("mock input")
}

object MockOutputEndpoint {
  def execute(input: com.github.notyy.typeflow.editor.WrappedOutput): Try[Unit] = Success[Unit](())
}
