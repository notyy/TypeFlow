package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

class GenAliyunlCallStatement(val serviceName: String) extends GenCallStatement {
  override def execute(outputParamName: String, resultName: String, targetDefinition: Definition): Option[String] = {
    val executeStatement = genExecuteStatement(targetDefinition, outputParamName)
    if (haveReturnType(targetDefinition)) {
      Some(s"val $resultName = $executeStatement.get.value")
    } else {
      Some(s"$executeStatement")
    }
  }

  private def genExecuteStatement(targetDefinition: Definition, outputParamName: String): String = {
    s"""AliyunUtil.callInstance(fcClient,Param($outputParamName),"$serviceName", "${targetDefinition.name}")"""
  }

  private def haveReturnType(targetDefinition: Definition) = {
    targetDefinition.outputs.size == 1 && targetDefinition.outputs.head.outputType.name != "Unit"
  }
}
