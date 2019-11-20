package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Output
import com.github.notyy.typeflow.util.TypeUtil

class GenJSonParamType4InputEndpoint {
  def execute(outputs: Vector[Output]): String = {
    if (outputs.isEmpty) {
      "Unit"
    } else if (outputs.size == 1) {
      val outputName = TypeUtil.removeDecorate(outputs.head.outputType.name)
      if (outputName == "Unit") "" else outputName
    } else {
      s"(${
        outputs.map { output =>
          TypeUtil.removeDecorate(output.outputType.name)
        }.reduce((x1, x2) => s"$x1,$x2")
      })"
    }
  }
}
