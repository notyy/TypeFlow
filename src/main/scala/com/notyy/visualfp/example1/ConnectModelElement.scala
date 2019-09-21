package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.UserInputIntepreter.ConnectElementCommand

object ConnectModelElement {
  def execute(savedModel: Model, connectElementCommand: ConnectElementCommand): Model = {
    val connection = Connection(connectElementCommand.from ,connectElementCommand.to)
    savedModel.copy(connections = savedModel.connections.appended(connection))
  }
}
