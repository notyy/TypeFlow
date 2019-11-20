package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Input
import com.github.notyy.typeflow.util.TypeUtil

class GenJSonParamType {
  def execute(inputs: Vector[Input]): String = {
    if (inputs.isEmpty) {
      "Unit"
    } else if (inputs.size == 1) {
      val inputName = TypeUtil.removeDecorate(inputs.head.inputType.name)
      if (inputName == "Unit") "" else inputName
    } else {
      s"(${
        inputs.map { input =>
          TypeUtil.removeDecorate(input.inputType.name)
        }.reduce((x1, x2) => s"$x1,$x2")
      })"
    }
  }
}
