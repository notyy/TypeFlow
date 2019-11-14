package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger

class GenCommandLineInputEndpoint {
  type InstanceId = String
  type InputIndex = Int
  type ParamName = String
  private val logger = Logger(this.getClass)
  //use mutable map to store param names for definition that requires multiple inputs
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, ParamName]] = scala.collection.mutable.Map.empty

  def execute(packageName: PackageName, commandLineInputEndpoint: CommandLineInputEndpoint, codeTemplate: CodeTemplate, model: Model): ScalaCode = {
    val flow: Flow = model.activeFlow.get
    val code = codeTemplate.value.replaceAllLiterally("$PackageName$", packageName.value).
      replaceAllLiterally("$DefinitionName$", commandLineInputEndpoint.name).
      replaceAllLiterally("$CallingChain$", prettifyStatements(accuStatements(flow.instances.find(_.id == commandLineInputEndpoint.name).get, 1, "input", flow.connections, flow.instances, Vector.empty)).mkString(System.lineSeparator()))
    ScalaCode(QualifiedName(s"${packageName.value}.${commandLineInputEndpoint.name}"), code)
  }

  def prettifyStatements(statements: Vector[String]): Vector[String] = statements.map(s => s"      $s")

  def accuStatements(outFrom: Instance, outputIndex: Int, outputParamName: String, connections: Vector[Connection], instances: Vector[Instance], currStatements: Vector[String]): Vector[String] = {
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
        val resultName = s"${targetInstanceId}Result"
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
      genCallStatement(outputParamName, resultName, targetDefinition)
    } else {
      if (waitingParams.contains(targetInstance.id)) {
        val prevParams = waitingParams(targetInstance.id)
        val currParams = prevParams + (targetInstanceInputIndex -> outputParamName)
        if (currParams.size == targetInstance.definition.inputs.size) {
          //enough parameters
          val params = currParams.toVector.sortBy(_._1).map(_._2).reduce((param1, param2) => s"$param1,$param2")
//          val statement = s"val $resultName = new ${targetDefinition.name}().execute($params)"
          val statement = genCallStatement(params, resultName,targetDefinition)
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

  private def genCallStatement(outputParamName: String, resultName: String, targetDefinition: Definition): Option[String] = {
    val executeStatement = genExecuteStatement(targetDefinition, outputParamName)
    if (haveReturnType(targetDefinition)) {
      Some(s"val $resultName = $executeStatement")
    } else {
      Some(s"$executeStatement")
    }
  }

  private def genExecuteStatement(targetDefinition: Definition, outputParamName: String): String = {
    s"new ${targetDefinition.name}().execute($outputParamName)"
  }

  private def haveReturnType(targetDefinition: Definition) = {
    targetDefinition.outputs.size == 1 && targetDefinition.outputs.head.outputType.name != "Unit"
  }
}
