package com.github.notyy.typeflow.editor

import scala.io.StdIn

object UserInputEndpoint {
  def execute(): UserInput = {
    UserInput(StdIn.readLine())
  }
}
