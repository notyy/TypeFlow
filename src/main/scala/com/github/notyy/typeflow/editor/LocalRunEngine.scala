package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, Function, InputEndpoint, Instance, Model, OutputEndpoint, OutputType}
import com.github.notyy.typeflow.util.{ReflectRunner, TypeUtil}
import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec

case class LocalRunEngine(model: Model, packagePrefix: Option[String]) {
  private val logger = Logger(classOf[LocalRunEngine])

  def startFlow(output: Any, outputFrom: Instance): Unit = {
    val nextInsts: Vector[Instance] = nextInstances(output, outputFrom)
    callNextInstances(output, nextInsts)
  }


  //@tailrec    TODO failed tailrec, must solve this later
  private def callNextInstances(output: Any, instances: Vector[Instance]): Unit = {
    instances.foreach { ins =>
      val nextOutput = callInstance(output, ins)
      val nextIns = nextInstances(nextOutput, ins)
      callNextInstances(nextOutput, nextIns)
    }
  }

  private def callInstance(output: Any, instance: Instance): Any = {
    logger.debug(s"calling ${instance.id} with parameter '$output'")
    val rs = ReflectRunner.run(instance.definition, packagePrefix, Some(output))
    logger.debug(s"result of calling ${instance.id} with parameter '$output' is '$rs'")
    rs
  }

  def nextInstances(output: Any, outputFrom: Instance): Vector[Instance] = {
    val flow = model.activeFlow
    val nIns = outputFrom match {
      case Instance(id,InputEndpoint(name, o)) => {
        val conns:Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
      case Instance(id, Function(name,inputType,outputs)) => {
        logger.debug(s"output from $id is $output, now looking for next instances")
        val outputType: OutputType = OutputType(TypeUtil.getTypeName(output))
        //TODO solve this .get later
        val index = outputs.find(_.outputType == outputType).get.index
        logger.debug(s"index is $index")
        val conns = flow.connections.filter(conn => conn.fromInstanceId == outputFrom.id && conn.outputIndex == index)
        logger.debug(s"find ${conns.size} connections")
        connections2instances(conns)
      }
      case Instance(id, OutputEndpoint(_,_,_,_)) => Vector.empty
    }
    logger.debug(s"next should call:[${nIns.map(_.id).mkString(",")}]")
    nIns
  }

  private def connections2instances(conns: Vector[Connection]): Vector[Instance] = {
    logger.debug(s"connections2Instances $conns")
    conns.map(_.toInstanceId).flatMap(insId => model.activeFlow.instances.filter(_.id == insId))
  }
}
