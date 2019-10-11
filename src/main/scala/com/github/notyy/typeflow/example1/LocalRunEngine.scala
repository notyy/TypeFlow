package com.github.notyy.typeflow.example1

import com.github.notyy.typeflow.util.{ReflectRunner, TypeUtil}

import scala.annotation.tailrec

case class LocalRunEngine(model: Model, inputEndPointName: String, packagePrefix: Option[String]) {
  def startFlow(output: Any, outputFrom: Instance): Unit = {
    val nextInsts: Vector[Instance] = nextInstances(output, outputFrom)
    callNextInstances(output, nextInsts)
  }


  def callNextInstances(output: Any, instances: Vector[Instance]): Vector[Any] = {
    instances.flatMap { ins =>
      val nextOutput = callInstance(output, ins)
      callNextInstances(nextOutput, nextInstances(nextOutput, ins))
    }
  }

  private def callInstance(output: Any, instance: Instance): Any = {
    ReflectRunner.run(instance.definition, packagePrefix, Some(output))
  }

  def nextInstances(output: Any, outputFrom: Instance): Vector[Instance] = {
    val flow = model.activeFlow
    outputFrom match {
      case Instance(id,InputEndpoint(name, o)) => {
        val conns:Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
      case Instance(id, Function(name,inputType,outputs)) => {
        val outputType: OutputType = OutputType(TypeUtil.getTypeShortName(output))
        //TODO solve this .get later
        val index = outputs.find(_.outputType == outputType).get.index
        val conns = flow.connections.filter(conn => conn.fromInstanceId == outputFrom.id && conn.outputIndex == index)
        connections2instances(conns)
      }
    }
  }

  private def connections2instances(conns: Vector[Connection]): Vector[Instance] = {
    conns.map(_.toInstanceId).flatMap(insId => model.activeFlow.instances.filter(_.id == insId))
  }
}
