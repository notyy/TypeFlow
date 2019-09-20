package com.notyy.visualfp.example1

import scala.io.StdIn

object UserInputEndpoint {
  def execute(): String = {
    StdIn.readLine()
  }
}
