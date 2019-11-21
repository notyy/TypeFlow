package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, Instance}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

class GenCallingChain(val genCallStatement: GenCallStatement) {
  type InstanceId = String
  type InputIndex = Int
  type ParamName = String

  private val logger = Logger(this.getClass)

  //use mutable map to store param names for definition that requires multiple inputs
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, ParamName]] = scala.collection.mutable.Map.empty
  private var startInstance: Option[Instance] = None

  def execute(outFrom: Instance, outputIndex: Int, outputParamName: String, connections: Vector[Connection], instances: Vector[Instance], statements: Vector[String]): Vector[String] = {
    startInstance = Some(outFrom)
    val accuStatementsResult = accuStatements(outFrom, Map(outputIndex -> outputParamName), connections, instances, statements)
    prettifyStatements(accuStatementsResult)
  }

  private def prettifyStatements(statements: Vector[String]): Vector[String] = statements.map(s => s"      $s")

  private def accuStatements(outFrom: Instance, outputParams: Map[Int, String], connections: Vector[Connection], instances: Vector[Instance], currStatements: Vector[String]): Vector[String] = {
    val connsFromThis = connections.filter(conn => conn.fromInstanceId == outFrom.id)
    if (connsFromThis.isEmpty) {
      logger.warn(s"no connection found for ${outFrom.id}")
      currStatements
    } else {
      connsFromThis.flatMap { conn =>
        val paramName = outputParams.getOrElse(conn.outputIndex, "")
        val targetInstanceId = conn.toInstanceId
        val targetInstanceInputIndex = conn.inputIndex
        val targetInstance = instances.find(_.id == targetInstanceId).get
        if (targetInstance == startInstance.get) {
          val statement: String = genResponse4request(paramName)
          currStatements.appended(statement)
        } else {
          val resultNamesMap = genResultName(targetInstance)
          val resultName = if (resultNamesMap.size == 1) resultNamesMap.head._2
          else s"(${resultNamesMap.values.mkString(",")})"
          //TODO only support java for now
          val statement = genCallStatements(paramName, targetInstance, targetInstanceInputIndex, resultName)
          if (statement.isDefined) {
            accuStatements(targetInstance, resultNamesMap, connections, instances, currStatements.appended(statement.get))
          } else {
            currStatements
          }
        }
      }
    }
  }

  private def genResultName(targetInstance: Instance): Map[Int,String] = {
    val outputs = targetInstance.definition.outputs
    val filterOutUnit = outputs.filterNot(ot => TypeUtil.removeDecorate(ot.outputType.name) == "Unit")
    filterOutUnit.map { o =>
      o.index -> TypeUtil.firstCharToLowercase(s"${targetInstance.id}Result${o.index}")
    }.toMap
  }

  private def genResponse4request(outputParamName: String) = {
    val statement = s"setResponse($outputParamName)"
    statement
  }

  private def genCallStatements(paramNames: String, targetInstance: Instance, targetInstanceInputIndex: Int, resultName: String): Option[String] = {
    val targetDefinition = targetInstance.definition
    val targetInputs = targetDefinition.inputs
    if (targetInputs.size == 1) {
      genCallStatement.execute(Vector(paramNames), resultName, targetDefinition)
    } else {
      if (waitingParams.contains(targetInstance.id)) {
        val prevParams = waitingParams(targetInstance.id)
        val currParams = prevParams + (targetInstanceInputIndex -> paramNames)
        if (currParams.size == targetInstance.definition.inputs.size) {
          //enough parameters
          val params = currParams.toVector.sortBy(_._1).map(_._2)
          //          val statement = s"val $resultName = new ${targetDefinition.name}().execute($params)"
          val statement = genCallStatement.execute(params, resultName, targetDefinition)
          waitingParams.remove(targetInstance.id)
          statement
        } else {
          waitingParams += (targetInstance.id -> currParams)
          None
        }
      } else {
        waitingParams += (targetInstance.id -> Map(targetInstanceInputIndex -> paramNames))
        None
      }
    }
  }
}
