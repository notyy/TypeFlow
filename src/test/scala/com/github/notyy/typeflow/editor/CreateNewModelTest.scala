package com.github.notyy.typeflow.editor

import java.io.File

import CreateModel.UnsavedModel
import CreateNewModel.ModelCreationSuccess
import org.scalatest.{FunSpec, Matchers}

class CreateNewModelTest extends FunSpec with Matchers {
    describe("SaveNewModel"){
      it("should create a new model file as $modelname.typeflow"){
        val unsavedModel = UnsavedModel("name1")
        val file = new File(s"./localOutput/${unsavedModel.modelName}.typeflow")
        file.delete()
        file shouldNot exist
        val rs = CreateNewModel.execute(unsavedModel)
        rs shouldBe ModelCreationSuccess(unsavedModel.modelName)
        file should exist
      }
    }
}
