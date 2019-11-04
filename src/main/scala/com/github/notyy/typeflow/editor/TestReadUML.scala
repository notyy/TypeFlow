package com.github.notyy.typeflow.editor

import scala.io.Source

object TestReadUML extends App {
  val source = Source.fromFile("localoutput/multi_param.puml")
  val rs = source.mkString
  source.close()
  PlantUML2Model.execute("multi_param",rs)
}
