package com.github.notyy.typeflow.editor

import java.io.File

import CreateNewModel.ModelCreationSuccess
import org.scalatest.{FunSpec, Matchers}

class CreateNewModelTest extends FunSpec with Matchers {
    describe("SaveNewModel"){
      it("should create a new model file as $modelname.typeflow"){
        val modelName = "name1"
        val file = new File(s"./localOutput/$modelName.typeflow")
        file.delete()
        file shouldNot exist
        val rs = CreateNewModel.execute(modelName)
        rs shouldBe ModelCreationSuccess(modelName)
        file should exist
      }
    }
}
