package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.InputType

object TypeUtil {
  def getTypeShortName(input: Any): String = {
    val rawName = input.getClass.getSimpleName
    if (rawName.endsWith("$")) rawName.init else rawName
  }

  def getTypeName(input: Any): String = {
    val typeName = input.getClass.getName
    if (typeName.startsWith("java.lang")) typeName else getTypeShortName(input)
  }

  def composeInputType(packagePrefix: Option[String], inputType: InputType) = {
    if (inputType.name.split('.').length > 1) {
      inputType.name
    } else if (PrimitiveTypeNameMap.contains(inputType.name)) {
      PrimitiveTypeNameMap(inputType.name)
    } else {
      packagePrefix.map(p => s"$p.${inputType.name}").getOrElse(inputType.name)
    }
  }

  val PrimitiveTypeNameMap: Map[String, String] = Map(
    "String" -> "java.lang.String",
    "Byte" -> "java.lang.Byte",
    "Short" -> "java.lang.Short",
    "Integer" -> "java.lang.Integer",
    "Long" -> "java.lang.Long",
    "Float" -> "java.lang.Float",
    "Double" -> "java.lang.Double",
    "Boolean" -> "java.lang.Boolean",
    "Character" -> "java.lang.Character"
  )
}
