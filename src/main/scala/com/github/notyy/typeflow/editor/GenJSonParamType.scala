package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Input

class GenJSonParamType {
  def execute(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      "Unit"
    } else if (inputs.size == 1) {
      val inputName = inputs.head.inputType.name
      if (inputName == "Unit") "" else inputName
    } else {
      s"(${
        inputs.map {
          _.inputType.name
        }.reduce((x1, x2) => s"$x1,$x2")
      })"
    }
  }
}
