package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, Instance}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class GenCallingChain(val genCallStatement: GenCallStatement) {
  type InstanceId = String
  type InputIndex = Int
  type ParamName = String

  private val logger = Logger(this.getClass)

  //use mutable map to store param names for definition that requires multiple inputs
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, ParamName]] = scala.collection.mutable.Map.empty
  private var startInstance: Option[Instance] = None
  private var statements: ArrayBuffer[String] = ArrayBuffer.empty

  def execute(outFrom: Instance, outputIndex: Int, outputParamName: String, connections: Vector[Connection], instances: Vector[Instance]): Vector[String] = {
    startInstance = Some(outFrom)
    accuStatements(outFrom, Map(outputIndex -> outputParamName), connections, instances)
    logger.debug("calling chain codes are:")
    statements.foreach(s => logger.debug(s))
    prettifyStatements(statements.toVector)
  }

  private def prettifyStatements(statements: Vector[String]): Vector[String] = statements.map(s => s"      $s")

  private def accuStatements(outFrom: Instance, outputParams: Map[Int, String], connections: Vector[Connection], instances: Vector[Instance]): Unit = {
    val connsFromThis = connections.filter(conn => conn.fromInstanceId == outFrom.id)
    if (connsFromThis.isEmpty) {
      logger.warn(s"no connection found for ${outFrom.id}")
    } else {
      connsFromThis.foreach { conn =>
        val paramName = outputParams.getOrElse(conn.outputIndex, "")
        val targetInstanceId = conn.toInstanceId
        val targetInstanceInputIndex = conn.inputIndex
        val targetInstance = instances.find(_.id == targetInstanceId).get
        if (targetInstance == startInstance.get) {
          val statement: String = genResponse4request(paramName)
          logger.debug(s"append statement: $statement")
          //          currStatements.appended(statement)
          statements += statement
        } else {
          val resultNamesMap = genResultName(targetInstance)
          //TODO only support java for now
          val statement = genCallStatements(paramName, targetInstance, targetInstanceInputIndex, resultNamesMap)
          if (statement.nonEmpty) {
            logger.debug(s"append statement: $statement")
            statements.appendAll(statement)
            accuStatements(targetInstance, resultNamesMap, connections, instances)
          } else {
            logger.warn(s"no statement generated for: $paramName, $targetInstance, $targetInstanceInputIndex, $resultNamesMap")
          }
        }
      }
    }
  }

  private def genResultName(targetInstance: Instance): Map[Int, String] = {
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

  private def genCallStatements(paramNames: String, targetInstance: Instance, targetInstanceInputIndex: Int, resultNamesMap: Map[Int,String]): Vector[String] = {
    val targetDefinition = targetInstance.definition
    val targetInputs = targetDefinition.inputs
    if (targetInputs.size == 1) {
      genCallStatement.execute(Vector(paramNames), resultNamesMap, targetDefinition)
    } else {
      if (waitingParams.contains(targetInstance.id)) {
        val prevParams = waitingParams(targetInstance.id)
        val currParams = prevParams + (targetInstanceInputIndex -> paramNames)
        if (currParams.size == targetInstance.definition.inputs.size) {
          //enough parameters
          val params = currParams.toVector.sortBy(_._1).map(_._2)
          //          val statement = s"val $resultName = new ${targetDefinition.name}().execute($params)"
          val statement = genCallStatement.execute(params, resultNamesMap, targetDefinition)
          waitingParams.remove(targetInstance.id)
          statement
        } else {
          waitingParams += (targetInstance.id -> currParams)
          Vector.empty
        }
      } else {
        waitingParams += (targetInstance.id -> Map(targetInstanceInputIndex -> paramNames))
        Vector.empty
      }
    }
  }
}
