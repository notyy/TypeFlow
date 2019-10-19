package com.github.notyy.typeflow.editor

import org.json4s.MappingException
import org.scalatest.{FunSpec, Matchers}

import scala.util.Failure

class Json2ModelTest extends FunSpec with Matchers {
  describe("Json2Model") {
    it("can transform from json string to Model") {
      val json = ReadFile.execute(Path("./src/main/resources/TypeFlowEditor.typeflow")).get
      val model = Json2Model.execute(json).get
      model.name shouldBe "typeflow_editor"
      model.definitions.foreach(println)
      println("----------------")
      model.definitions.size shouldBe 4
      model.activeFlow.get.name shouldBe "minimalFlow"
      model.activeFlow.get.connections.foreach(println)
      model.activeFlow.get.connections.size shouldBe 20
    }
    it("should return new model when processing empty string") {
      val tryModel = Json2Model.execute("")
      tryModel match {
        case Failure(ex) => succeed
        case _ => fail
      }
    }
  }
}
