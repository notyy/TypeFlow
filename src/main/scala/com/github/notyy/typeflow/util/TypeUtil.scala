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
    val inputTypeName = ModelUtil.removePrefix(inputType.name)
    if (inputTypeName.split('.').length > 1) {
      inputTypeName
    } else if (PrimitiveTypeNameMap.contains(inputTypeName)) {
      PrimitiveTypeNameMap(inputTypeName)
    } else {
      packagePrefix.map(p => s"$p.${inputTypeName}").getOrElse(inputTypeName)
    }
  }

  def removeDecorate(typeName: String): String = typeName.split("::").last

  val PrimitiveTypeNameMap: Map[String, String] = Map(
    "String" -> "java.lang.String",
    "Byte" -> "java.lang.Byte",
    "Short" -> "java.lang.Short",
    "Integer" -> "java.lang.Integer",
    "Long" -> "java.lang.Long",
    "Float" -> "java.lang.Float",
    "Double" -> "java.lang.Double",
    "Boolean" -> "java.lang.Boolean",
    "Character" -> "java.lang.Character",
    "Object" -> "java.lang.Object",
    "Unit" -> "scala.runtime.BoxedUnit"
  )
}
