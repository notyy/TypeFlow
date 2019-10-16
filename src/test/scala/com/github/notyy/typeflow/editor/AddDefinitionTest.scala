package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import org.scalatest.{FunSpec, Matchers}

class AddDefinitionTest extends FunSpec with Matchers {
  describe("AddDefinition"){
    it("allow's user to add new Definition to a given model"){
      val model = Model("testModel", Vector.empty,Vector.empty,None)
    }
  }
}
