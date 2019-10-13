package com.github.notyy.typeflow.util

object TypeUtil {
  def getTypeShortName(input: Any): String = {
    val rawName = input.getClass.getSimpleName
    val name = if(rawName.endsWith("$")) rawName.init else rawName
    println(s"type rawName is : $name")
    name
  }

  def getTypeName(input: Any): String = {
    val typeName = input.getClass.getName
    if(typeName.startsWith("java.lang")) typeName else getTypeShortName(input)
  }
}
