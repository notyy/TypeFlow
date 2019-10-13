package com.github.notyy.typeflow.example1

import scala.util.{Success, Try}

object CommandLineOutputEndpoint {
  def execute(output: String):Try[Unit] = {
    CommandLineUI.onResponse(output)
    Success()
  }
}
