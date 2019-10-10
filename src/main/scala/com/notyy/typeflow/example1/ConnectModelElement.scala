package com.notyy.typeflow.example1

import com.notyy.typeflow.example1.UserInputInterpreter.ConnectInstanceCommand

object ConnectModelElement {
  def execute(savedModel: Model, connectElementCommand: ConnectInstanceCommand): Model = {
    val connection = Connection(connectElementCommand.fromInstanceId,connectElementCommand.outputIndex ,connectElementCommand.toInstanceId)
    //TODO temporary put here, will deal with it later
    val flowIndex = savedModel.flows.indexWhere(_.name == connectElementCommand.flowName)
    val updatedFlow = savedModel.flows(flowIndex).copy(connections = savedModel.flows(flowIndex).connections.appended(connection))
    savedModel.copy(flows = savedModel.flows.updated(flowIndex,updatedFlow))
  }
}
