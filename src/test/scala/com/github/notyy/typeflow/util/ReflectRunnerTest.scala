package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.editor.UserInputInterpreter.{QuitCommand, UnknownCommand}
import com.github.notyy.typeflow.editor._
import org.scalatest.{FunSpec, Matchers}

import scala.util.{Success, Try}

class ReflectRunnerTest extends FunSpec with Matchers {
  val packgePrefix = Some("com.github.notyy.typeflow.editor")
  describe("ReflectRunner") {
    it("should run defined function by name") {
      val userInputInterpreter: Function = Function("UserInputInterpreter", InputType("java.lang.String"),
        outputs = Vector(
          Output(OutputType("UnknownCommand"), 1),
          Output(OutputType("QuitCommand"), 2)
        ))
      ReflectRunner.run(userInputInterpreter, packgePrefix, Some(":q")).
        asInstanceOf[UserInputInterpreter.InterpreterResult] shouldBe QuitCommand
      ReflectRunner.run(userInputInterpreter, packgePrefix, Some("badcommand")).
        asInstanceOf[UserInputInterpreter.InterpreterResult] shouldBe UnknownCommand("badcommand")
    }
    it("should run InputEndpoint") {
      val mockInputEndpoint: InputEndpoint = InputEndpoint("com.github.notyy.typeflow.util.MockInputEndpoint", OutputType("java.lang.String"))
      ReflectRunner.run(mockInputEndpoint, None, None).asInstanceOf[String] shouldBe "mock input"
    }
    it("should run OutputEndpoint") {
      val mockOutputEndpint: OutputEndpoint = OutputEndpoint("com.github.notyy.typeflow.util.MockOutputEndpoint",InputType("java.lang.String"), OutputType("Unit"),Vector.empty)
      ReflectRunner.run(mockOutputEndpint,None,Some("input")) shouldBe ()
    }
  }
}

object MockInputEndpoint {
  def execute(): String = "mock input"
}

object MockOutputEndpoint {
  def execute(input: String): Try[Unit] = Success("mock output resp")
}
