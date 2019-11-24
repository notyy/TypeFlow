package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

class GenLocalCallStatement extends GenCallStatement {
  override def execute(paramNames: Vector[String], resultNamesMap: Map[Int, String], targetDefinition: Definition): Vector[String] = {
    val executeStatement = genExecuteStatement(targetDefinition, paramNames)
    if (haveReturnType(targetDefinition)) {
      if (resultNamesMap.size == 1) {
        Vector(s"val ${resultNamesMap.head._2} = $executeStatement")
      } else {
        val tupleReturnName = resultNamesMap.head._2.init
        val returnLine = s"val $tupleReturnName  = $executeStatement"
        val splitLines = resultNamesMap.toSeq.sortBy(_._1).map{
          case (i,name) => s"val $name = $tupleReturnName._$i"
        }
        splitLines.prepended(returnLine).toVector
      }
    } else {
      Vector(s"$executeStatement")
    }
  }

  private def genExecuteStatement(targetDefinition: Definition, outputParamNames: Vector[String]): String = {
    val params = if (outputParamNames.size == 1) {
      outputParamNames.head
    } else {
      outputParamNames.reduce((param1, param2) => s"$param1,$param2")
    }
    s"new ${targetDefinition.name}().execute($params)"
  }

  private def haveReturnType(targetDefinition: Definition) = {
    targetDefinition.outputs.nonEmpty && targetDefinition.outputs.head.outputType.name != "Unit"
  }
}
