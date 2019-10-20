package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.Fixtures
import com.github.notyy.typeflow.domain.{Connection, Flow, Instance, Model}
import org.scalatest.{FunSpec, Matchers}

class ConnectModelElementTest extends FunSpec with Matchers {
  val flow: Flow = Flow("flow", Vector(Instance(Fixtures.numInput), Instance(Fixtures.add2)), Vector.empty)
  val definitions = Vector(Fixtures.numInput, Fixtures.add2)
  val model: Model = Model("sampleModel",
    definitions,
    flows = Vector(flow), Some(flow))

  describe("ConnectModelElement") {
    it("can connect two definition") {
      val connectElementCommand = ConnectElementCommand(
        Fixtures.numInput.name, 1, Fixtures.add2.name, 1, "sampleModel")
      val newModel = ConnectModelElement.execute(model, connectElementCommand)
      val conns = newModel.activeFlow.get.connections
      conns.size shouldBe 1
      conns.head shouldBe Connection(Fixtures.numInput.name, 1, Fixtures.add2.name, 1)
    }
    it("can auto gen instance for definition, when connecting two definitions") {
      val noInstanceFlow: Flow = Flow("noInstanceFlow", Vector.empty, Vector.empty)
      val testModel = Model("testModel",definitions,Vector(noInstanceFlow), Some(noInstanceFlow))
      testModel.activeFlow.get.instances.size shouldBe 0
      val connectElementCommand = ConnectElementCommand(
        Fixtures.numInput.name, 1, Fixtures.add2.name, 1, "testModel")
      val newModel = ConnectModelElement.execute(testModel, connectElementCommand)
      val conns = newModel.activeFlow.get.connections
      conns.size shouldBe 1
      conns.head shouldBe Connection(Fixtures.numInput.name, 1, Fixtures.add2.name, 1)
      newModel.activeFlow.get.instances.size shouldBe 2
    }
  }
}
