package com.github.notyy.typeflow.util

object TypeUtil {
  def getTypeShortName(input: Any): String = {
    val rawName = input.getClass.getSimpleName
    val name = if(rawName.endsWith("$")) rawName.init else rawName
    println(s"type rawName is : $name")
    name
  }
}
