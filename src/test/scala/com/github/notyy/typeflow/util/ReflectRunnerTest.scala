package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain
import com.github.notyy.typeflow.domain._
import com.github.notyy.typeflow.editor.{InterpreterResult, QuitCommand, UnknownCommand, UserInput, UserInputInterpreter, WrappedOutput}
import org.scalatest.{FunSpec, Matchers}

import scala.util.{Success, Try}

class ReflectRunnerTest extends FunSpec with Matchers {
  val packgePrefix = Some("com.github.notyy.typeflow.editor")
  describe("ReflectRunner") {
    it("should run defined function by name") {
      val userInputInterpreter: domain.PureFunction = domain.PureFunction("UserInputInterpreter", Vector(Input(InputType("UserInput"),1)),
        outputs = Vector(
          Output(OutputType("UnknownCommand"), 1),
          Output(OutputType("QuitCommand"), 2)
        ))
      ReflectRunner.run(userInputInterpreter, packgePrefix, Some(UserInput(":q"))).
        asInstanceOf[InterpreterResult] shouldBe QuitCommand
      ReflectRunner.run(userInputInterpreter, packgePrefix, Some(UserInput("badcommand"))).
        asInstanceOf[InterpreterResult] shouldBe UnknownCommand("badcommand")
    }
    it("should run InputEndpoint") {
      val mockInputEndpoint: InputEndpoint = InputEndpoint("com.github.notyy.typeflow.util.MockInputEndpoint", OutputType("UserInput"))
      val rs = ReflectRunner.run(mockInputEndpoint, None, None)
      println(s"rs=$rs")
      rs.asInstanceOf[UserInput] shouldBe UserInput("mock input")
    }
    it("should run OutputEndpoint") {
      val mockOutputEndpint: OutputEndpoint = OutputEndpoint("com.github.notyy.typeflow.util.MockOutputEndpoint",InputType("com.github.notyy.typeflow.editor.WrappedOutput"), OutputType("Unit"),Vector.empty)
      ReflectRunner.run(mockOutputEndpint,None,Some(WrappedOutput("input"))) shouldBe ()
    }
  }
}

object MockInputEndpoint {
  def execute(): UserInput = UserInput("mock input")
}

object MockOutputEndpoint {
  def execute(input: com.github.notyy.typeflow.editor.WrappedOutput): Try[Unit] = Success[Unit](())
}
