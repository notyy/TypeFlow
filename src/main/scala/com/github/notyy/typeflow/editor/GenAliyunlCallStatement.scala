package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

class GenAliyunlCallStatement(val serviceName: String) extends GenCallStatement {
  override def execute(paramNames: Vector[String], resultNamesMap: Map[Int, String], targetDefinition: Definition): Vector[String] = {
    val executeStatement = genExecuteStatement(targetDefinition, paramNames)
    if (haveReturnType(targetDefinition)) {
      if (resultNamesMap.size == 1) {
        Vector(s"val ${resultNamesMap.head._2} = $executeStatement.get")
      } else {
        val tupleReturnName = resultNamesMap.head._2.init
        val returnLine = s"val $tupleReturnName  = $executeStatement.get"
        val splitLines = resultNamesMap.toSeq.sortBy(_._1).map {
          case (i, name) => s"val $name = $tupleReturnName._i"
        }
        Vector(splitLines.prepended(returnLine).mkString(System.lineSeparator()))
      }
    } else {
      Vector(s"$executeStatement.get")
    }
  }

  private def genExecuteStatement(targetDefinition: Definition, outputParamNames: Vector[String]): String = {
    val params:Option[String] =
      if(outputParamNames.isEmpty){
        None
      } else if (outputParamNames.size == 1) {
        val outputParamName = outputParamNames.head
        if(outputParamName.isEmpty || outputParamName == "Unit") {
          None
        } else {
          Some(outputParamName)
        }
    } else {
      Some(outputParamNames.reduce((param1, param2) => s"Param($param1.value,$param2.value)"))
    }
    s"""AliyunUtil.callInstance(fcClient,$params,"$serviceName", "${targetDefinition.name}")"""
  }

  private def haveReturnType(targetDefinition: Definition) = {
    targetDefinition.outputs.nonEmpty && targetDefinition.outputs.head.outputType.name != "Unit"
  }
}
