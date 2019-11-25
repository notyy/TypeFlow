package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Output

class GenWriteOut {
  def execute(outputs: Vector[Output]): String = {
    if (outputs.isEmpty || outputs.head.outputType.name == "Unit") "" else "output.write(JSONUtil.toJSON(Param(value)).getBytes)"
  }
}
