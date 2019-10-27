package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.{Fixtures, domain}
import com.github.notyy.typeflow.domain._
import org.scalatest.{FunSpec, Matchers}

import scala.util.Try

class LocalRunEngineTest extends FunSpec with Matchers {

  describe("LocalRunEngine") {
    it("can find next instance by the type of input") {
      val x: Any = QuitCommand
      val localRunEngine = LocalRunEngine(Fixtures.model, Some("com.github.notyy.typeflow.example1"))
      localRunEngine.nextInstances(":q", Instance(Fixtures.userInputEndpoint)) shouldBe Vector((Instance(Fixtures.userInputInterpreter), 1))
      localRunEngine.nextInstances(QuitCommand, Instance(Fixtures.userInputInterpreter)) shouldBe Vector((Instance(Fixtures.wrapOutput), 1))
      localRunEngine.nextInstances(ModelCreationSuccess("newModel"), Instance(Fixtures.createNewModel)) shouldBe Vector((Instance(Fixtures.wrapOutput), 1))
    }
    //TODO will fix this for scala local run
//    it("can call function with multiple parameters") {
//      val localRunEngine = LocalRunEngine(Fixtures.multiParamModel, Some("com.github.notyy.typeflow.editor"))
//      localRunEngine.startFlow(3, Fixtures.multiParamModel.activeFlow.get.instances(0))
//    }
  }
}

object Add2 {
  def execute(input: Integer): Integer = input + 2
}

object Multi3 {
  def execute(input: Integer): Integer = input * 3
}

object AddAndPrint {
  def execute(x: Integer, y: Integer): Try[Integer] = Try {
    val rs = x + y
    println(s"$x + $y = $rs")
    rs
  }
}

object PrintEP {
  def execute(x: Integer): Try[Integer] = Try {
    println(x)
    x
  }
}
