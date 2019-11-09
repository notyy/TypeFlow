package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Input

class GenFormalParams {
  def execute(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      ""
    } else {
      inputs.map { input =>
        val inputTypeName = input.inputType.name
        if (inputTypeName == "Unit") ""
        else {
          val inputParamTypeName = if (inputTypeName == "Unit") "" else inputTypeName
          val paramName = s"param${input.index}"
          s"$inputParamTypeName $paramName"
        }
      }.reduce((x1, x2) => s"$x1,$x2")
    }
  }
}
