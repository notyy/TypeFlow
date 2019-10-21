package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import org.scalatest.{FunSpec, Matchers}

class CreateFlowTest extends FunSpec with Matchers {
  val model = Model("sampleModel", Vector.empty, Vector.empty, None)

  describe("CreateFlow") {
    it("can create flow in model, and make the new model as the active flow") {
      val createFlowCommand = CreateFlowCommand("sampleModel","sampleFlow")
      val updatedModel = CreateFlow.execute(model, createFlowCommand)
      updatedModel.flows.size shouldBe 1
      updatedModel.activeFlow.get.name shouldBe "sampleFlow"
    }
  }
}
