package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.{Fixtures, domain}
import com.github.notyy.typeflow.domain._
import org.scalatest.{FunSpec, Matchers}

class LocalRunEngineTest extends FunSpec with Matchers {

  val model = Fixtures.model

  describe("LocalRunEngine"){
    it("can find next instance by the type of input"){
      val x:Any = QuitCommand
      val localRunEngine = LocalRunEngine(model,Some("com.github.notyy.typeflow.example1"))
      localRunEngine.nextInstances(":q",Instance(Fixtures.userInputEndpoint)) shouldBe Vector(Instance(Fixtures.userInputInterpreter))
      localRunEngine.nextInstances(QuitCommand,Instance(Fixtures.userInputInterpreter)) shouldBe Vector(Instance(Fixtures.wrapOutput))
    }
  }
}
