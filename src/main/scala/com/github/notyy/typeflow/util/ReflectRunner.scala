package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.{Definition, Function, InputEndpoint, OutputEndpoint}

import scala.util.Try

object ReflectRunner {
  def run(definition: Definition, packagePrefix: Option[String], input: Option[Any]): Any = {
    definition match {
      case Function(name,inputType,_) => {
        locateClass(packagePrefix, name).
          getDeclaredMethod("execute", Class.forName(inputType.name)).
          invoke(null, input.get)
      }
      case InputEndpoint(name, output) => {
        locateClass(packagePrefix, name).
          getDeclaredMethod("execute").invoke(null)
      }
      case OutputEndpoint(name, inputType, outputType, errorOutput) => {
        locateClass(packagePrefix, name).
          getDeclaredMethod("execute",Class.forName(inputType.name)).
          invoke(null, input.get).asInstanceOf[Try[Any]].getOrElse(new IllegalStateException(s"error running $name"))
      }
      case _ => ???
    }
  }

  private def locateClass(packagePrefix: Option[String], name: String) = {
    Class.forName(packagePrefix.map(prefix => s"$prefix.$name").getOrElse(name))
  }
}
