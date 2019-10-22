package com.github.notyy.typeflow.editor

import org.scalatest.{FunSpec, Matchers}

class DiffTest extends FunSpec with Matchers {
  describe("Diff") {
    it("can compare two PlantUML and produce a new PlantUML that highlight the differences") {
      val newModel = ReadFile.execute(Path("./fixtures/diff/newModel.puml")).get
      val multi_param = ReadFile.execute(Path("./fixtures/diff/multi_param.puml")).get
      val diff = Diff.execute(multi_param, newModel)
      SaveToFile.execute(Path("./localoutput/diff/diff.puml"),diff)
    }
  }
}
