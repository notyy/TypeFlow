package com.github.notyy.typeflow.util

object TypeUtil {
  def getTypeShortName(input: Any): String = {
    val rawName = input.getClass.getSimpleName
    if (rawName.endsWith("$")) rawName.init else rawName
  }

  def getTypeName(input: Any): String = {
    val typeName = input.getClass.getName
    if(typeName.startsWith("java.lang")) typeName else getTypeShortName(input)
  }
}
