package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain._
import com.github.notyy.typeflow.util.{ModelUtil, ReflectRunner, TypeUtil}
import com.typesafe.scalalogging.Logger

import scala.util.Try


case class LocalRunEngine(model: Model, packagePrefix: Option[String]) {
  private val logger = Logger(classOf[LocalRunEngine])

  type InstanceId = String
  type InputIndex = Int
  //TODO for function that have multiple inputs, when we got one input ready,
  //we have to put it to cache, waiting for other inputs to be ready.
  //this maybe dangerous, because it occupy uncontrolled num of memory.
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, Any]] = scala.collection.mutable.Map.empty

  def startFlow(output: Any, outputFrom: Instance): Unit = {
    val nextInsts: Vector[(Instance, InputIndex)] = nextInstances(output, outputFrom)
    callNextInstances(output, nextInsts)
  }

  //@tailrec    TODO failed tailrec, must solve this later
  private def callNextInstances(output: Any, instances: Vector[(Instance, InputIndex)]): Unit = {
    instances.foreach { case (ins, idx) =>
      if (ins.definition.inputs.size > 1) {
        if (waitingParams.contains(ins.id)) {
          val prevParams = waitingParams(ins.id)
          val currParams = prevParams + (idx -> output)
          if (currParams.size == ins.definition.inputs.size) {
            //enough parameters
            val nextOutput = callInstance(currParams.toVector.sortBy(_._1).map(_._2), ins)
            waitingParams.remove(ins.id)
            val nextIns = nextInstances(nextOutput, ins)
            callNextInstances(nextOutput, nextIns)
          } else {
            waitingParams += (ins.id -> currParams)
          }
        } else {
          waitingParams += (ins.id -> Map(idx -> output))
        }
      } else {
        val nextOutput = callInstance(Vector(output), ins)
        val nextIns = nextInstances(nextOutput, ins)
        callNextInstances(nextOutput, nextIns)
      }
    }
  }

  private def callInstance(outputs: Vector[Any], instance: Instance): Any = {
    logger.debug(s"calling ${instance.id} with parameter '$outputs'")
    val rs = ReflectRunner.run(instance.definition, packagePrefix, Some(outputs))
    logger.debug(s"result of calling ${instance.id} with parameter '$outputs' is '$rs'")
    rs
  }

  def nextInstances(output: Any, outputFrom: Instance): Vector[(Instance, InputIndex)] = {
    logger.debug(s"looking for nextInstance for ${outputFrom.id}.$output")
    val flow = model.activeFlow.get
    val nIns = outputFrom match {
      case Instance(id, InputEndpoint(name, o)) => {
        logger.debug(s"outputs from InputEndpoint $id is $output, now looking for next instances")
        val conns: Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
      case Instance(id, PureFunction(name, inputType, outputs)) => {
        logger.debug(s"outputs from PureFunction $id is $output, now looking for next instances")
        val outputType: OutputType = OutputType(TypeUtil.getTypeShortName(output))
        logger.debug(s"outputType name of parameter is ${outputType.name}")
        logger.debug(s"PureFunction $name's designed outputs are ${outputs.mkString(",")}")
        //TODO solve this .get later
        val index = outputs.find { ot =>
          val designedOutputTypeName = ModelUtil.removePrefix(ot.outputType.name).split('.').last
          logger.debug(s"comparing designed outputTypeName $designedOutputTypeName to actually outputTypeName ${outputType.name}")
          designedOutputTypeName == outputType.name
        }.get.index
        //        logger.debug(s"index is $index")
        val conns = flow.connections.filter(conn => conn.fromInstanceId == outputFrom.id && conn.outputIndex == index)
        //        logger.debug(s"find ${conns.size} connections")
        connections2instances(conns)
      }
      case Instance(id, OutputEndpoint(name, inputs, outputType, errorOutputs)) => {
        logger.debug(s"outputs from OutputEndpoint $id is $output, now looking for next instances")
        val conns: Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
    }
    logger.debug(s"next should call:[${nIns.map { case (ins, idx) => s"${ins.id}.$idx" }.mkString(",")}]")
    nIns
  }

  private def connections2instances(conns: Vector[Connection]): Vector[(Instance, InputIndex)] = {
    logger.debug(s"connections2Instances $conns")
    conns.map(conn => (model.activeFlow.get.instances.find(_.id == conn.toInstanceId).get, conn.inputIndex))
  }
}

object LocalRunEngine {
  def runFlow(modelFilePath: Path, inputEndpointName: String, packageName: String, output: Any): Try[Unit] = {
    ReadFile.execute(modelFilePath).map { puml =>
      val model = PlantUML2Model.execute(PlantUML(modelFilePath.value.dropRight(5).split('/').last, puml))
      val inputEndpoint = model.activeFlow.get.instances.find(_.id == inputEndpointName).get
      val localRunEngine = LocalRunEngine(model, Some(packageName))
      localRunEngine.startFlow(output, inputEndpoint)
    }
  }
}
