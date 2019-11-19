package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, Definition, Instance}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

class GenCallingChain(val genCallStatement: GenCallStatement) {
  type InstanceId = String
  type InputIndex = Int
  type ParamName = String

  private val logger = Logger(this.getClass)

  //use mutable map to store param names for definition that requires multiple inputs
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, ParamName]] = scala.collection.mutable.Map.empty

  def execute(outFrom: Instance, outputIndex: Int, outputParamName: String, connections: Vector[Connection], instances: Vector[Instance],statements: Vector[String]): Vector[String] = {
    prettifyStatements(accuStatements(outFrom,outputIndex,outputParamName,connections,instances,statements))
  }

  private def prettifyStatements(statements: Vector[String]): Vector[String] = statements.map(s => s"      $s")

  private def accuStatements(outFrom: Instance, outputIndex: Int, outputParamName: String, connections: Vector[Connection], instances: Vector[Instance], currStatements: Vector[String]): Vector[String] = {
    val connsFromThis = connections.filter(conn => conn.fromInstanceId == outFrom.id && conn.outputIndex == outputIndex)
    if (connsFromThis.isEmpty) {
      logger.warn(s"no connection found for ${outFrom.id}.$outputIndex")
      currStatements
    } else {
      connsFromThis.flatMap { conn =>
        val targetInstanceId = conn.toInstanceId
        val targetInstanceInputIndex = conn.inputIndex
        val targetInstance = instances.find(_.id == targetInstanceId).get
        val targetDefinition = targetInstance.definition
        val resultName = TypeUtil.firstCharToLowercase(s"${targetInstanceId}Result")
        //TODO only support java for now
        val targetOutputs = targetDefinition.outputs
        if (targetOutputs.size == 1) {
          val statement = genCallStatements(outputParamName, targetInstance, targetInstanceInputIndex, resultName)
          if (statement.isDefined) {
            accuStatements(targetInstance, 1, resultName, connections, instances, currStatements.appended(statement.get))
          } else {
            currStatements
          }
        } else {
          //TODO complete this
          logger.error("multiple output not supported yet")
          currStatements
        }
      }
    }
  }

  private def genCallStatements(outputParamName: String, targetInstance: Instance, targetInstanceInputIndex: Int, resultName: String): Option[String] = {
    val targetDefinition = targetInstance.definition
    val targetInputs = targetDefinition.inputs
    if (targetInputs.size == 1) {
      genCallStatement.execute(outputParamName, resultName, targetDefinition)
    } else {
      if (waitingParams.contains(targetInstance.id)) {
        val prevParams = waitingParams(targetInstance.id)
        val currParams = prevParams + (targetInstanceInputIndex -> outputParamName)
        if (currParams.size == targetInstance.definition.inputs.size) {
          //enough parameters
          val params = currParams.toVector.sortBy(_._1).map(_._2).reduce((param1, param2) => s"$param1,$param2")
          //          val statement = s"val $resultName = new ${targetDefinition.name}().execute($params)"
          val statement = genCallStatement.execute(params, resultName,targetDefinition)
          waitingParams.remove(targetInstance.id)
          statement
        } else {
          waitingParams += (targetInstance.id -> currParams)
          None
        }
      } else {
        waitingParams += (targetInstance.id -> Map(targetInstanceInputIndex -> outputParamName))
        None
      }
    }
  }
}
