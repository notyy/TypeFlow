package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

trait GenCallStatement {
  def execute(paramNames: Vector[String], resultName: String, targetDefinition: Definition): Option[String]
}
