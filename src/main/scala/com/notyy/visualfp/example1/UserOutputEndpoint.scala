package com.notyy.visualfp.example1

object UserOutputEndpoint {
  def execute(output: String):String = {
    println(output)
    output
  }
}
