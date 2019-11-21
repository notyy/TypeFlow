package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

class GenAliyunlCallStatement(val serviceName: String) extends GenCallStatement {
  override def execute(outputParamNames: Vector[String], resultName: String, targetDefinition: Definition): Option[String] = {
    val executeStatement = genExecuteStatement(targetDefinition, outputParamNames)
    if (haveReturnType(targetDefinition)) {
      Some(s"val $resultName = $executeStatement.get")
    } else {
      Some(s"$executeStatement.get")
    }
  }

  private def genExecuteStatement(targetDefinition: Definition, outputParamNames: Vector[String]): String = {
    val params = if (outputParamNames.size == 1) {
      outputParamNames.head
    } else {
      outputParamNames.reduce((param1, param2) => s"Param($param1.value,$param2.value)")
    }
    s"""AliyunUtil.callInstance(fcClient,$params,"$serviceName", "${targetDefinition.name}")"""
  }

  private def haveReturnType(targetDefinition: Definition) = {
    targetDefinition.outputs.size == 1 && targetDefinition.outputs.head.outputType.name != "Unit"
  }
}