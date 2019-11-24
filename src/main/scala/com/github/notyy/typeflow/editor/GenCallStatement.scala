package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.Definition

trait GenCallStatement {
  def execute(paramNames: Vector[String], resultNamesMap: Map[Int, String], targetDefinition: Definition): Vector[String]
}
