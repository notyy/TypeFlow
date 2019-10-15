package com.github.notyy.typeflow.editor

import scala.util.{Success, Try}

object CommandLineOutputEndpoint {
  def execute(output: WrappedOutput):Try[Unit] = {
    CommandLineUI.onResponse(output.value)
    Success(())
  }
}
