package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, Definition, InputEndpoint, Instance, Model, OutputEndpoint, PureFunction}

object ConnectModelElement {
  def execute(savedModel: Model, connectElementCommand: ConnectElementCommand): Model = {
    val currDefis = savedModel.definitions
    val currInstances = savedModel.activeFlow.get.instances
    val updInstances = currInstances ++ createInstanceIfMissing(currDefis, currInstances, Vector(connectElementCommand.fromInstanceId, connectElementCommand.toInstanceId))
    val connection = Connection(
      connectElementCommand.fromInstanceId,
      connectElementCommand.fromInputIndex,
      connectElementCommand.toInstanceId,
      connectElementCommand.toInputIndex
    )
    val activeFlow = savedModel.activeFlow.get
    //TODO temporary put here, will deal with it later
    val updatedFlow = activeFlow.copy(instances = updInstances, connections = activeFlow.connections.appended(connection))
    savedModel.copy(activeFlow = Some(updatedFlow))
  }

//  def outputTypeName2index(instanceId: String, outputTypeName: String, instances: Vector[Instance]): Int = {
//    instances.find(_.id == instanceId).get.definition match {
//      case InputEndpoint(name, outputType) => 1
//      case OutputEndpoint(name, inputType, outputType, errorOutputs) => 1
//      case PureFunction(name, inputs, outputs) => outputs.find(_.outputType.name == outputTypeName).get.index
//    }
//  }

  def createInstanceIfMissing(currDefis: Vector[Definition], currInstances: Vector[Instance], instanceIds: Vector[String]): Vector[Instance] = {
    instanceIds.filterNot(currInstances.contains).map(id => Instance(id, currDefis.find(_.name == id).get))
  }
}
