package com.github.notyy.typeflow.editor

import scala.util.{Success, Try}

object CommandLineOutputEndpoint {
  def execute(output: String):Try[Unit] = {
    CommandLineUI.onResponse(output)
    Success()
  }
}
