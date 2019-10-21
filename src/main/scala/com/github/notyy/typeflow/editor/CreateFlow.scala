package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{Flow, Model}

object CreateFlow {
  def execute(savedModel: Model, createFlowCommand: CreateFlowCommand): Model = {
    val newFlow = Flow(createFlowCommand.name,Vector.empty,Vector.empty)
    savedModel.copy(flows = Vector(newFlow), activeFlow = Some(newFlow))
  }
}
