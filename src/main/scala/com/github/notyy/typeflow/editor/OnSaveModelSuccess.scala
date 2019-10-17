package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Model
import com.github.notyy.typeflow.editor.UpdateModel.ModelUpdateSuccess

object OnSaveModelSuccess {
  def execute(model: Model, s:Unit): ModelUpdateSuccess = ModelUpdateSuccess(model.name)
}
