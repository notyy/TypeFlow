package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Connection, InputEndpoint, Model, OutputEndpoint,PureFunction}

object ConnectModelElement {
  def execute(savedModel: Model, connectElementCommand: ConnectElementCommand): Model = {
    val instanceId = connectElementCommand.fromInstanceId
    val connection = Connection(instanceId,
      outputTypeName2index(instanceId,connectElementCommand.outputTypeName,savedModel),
      connectElementCommand.toInstanceId,
      connectElementCommand.toInputIndex
    )
    //TODO temporary put here, will deal with it later
    val flowIndex = savedModel.flows.indexWhere(_.name == connectElementCommand.flowName)
    val updatedFlow = savedModel.flows(flowIndex).copy(connections = savedModel.flows(flowIndex).connections.appended(connection))
    savedModel.copy(flows = savedModel.flows.updated(flowIndex,updatedFlow))
  }

  def outputTypeName2index(instanceId: String,outputTypeName: String, model: Model):Int = {
    model.activeFlow.get.instances.find(_.id == instanceId).get.definition match {
      case InputEndpoint(name, outputType) => 1
      case OutputEndpoint(name, inputType, outputType, errorOutputs) => 1
      case PureFunction(name, inputs,outputs) => outputs.find(_.outputType.name == outputTypeName).get.index
    }
  }
}
