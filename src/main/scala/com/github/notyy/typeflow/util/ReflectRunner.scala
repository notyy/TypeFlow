package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger

import scala.util.Try

object ReflectRunner {
  private val logger = Logger("ReflectRunner")

  def run(definition: Definition, packagePrefix: Option[String], input: Option[Any]): Any = {
    logger.debug(s"input.getClass is ${input.map(_.getClass.getName).getOrElse("no argument")}")
    definition match {
      case Function(name, inputs, _) => {
        locateClass(packagePrefix, name).
          getDeclaredMethod("execute", inputs.map(input => Class.forName(composeInputType(packagePrefix, input.inputType))):_*).
          invoke(null, input.get)
      }
      case InputEndpoint(name, output) => {
        locateClass(packagePrefix, name).
          getDeclaredMethod("execute").invoke(null)
      }
      case OutputEndpoint(name, inputType, outputType, errorOutput) => {
//        val method = locateClass(packagePrefix, name).getDeclaredMethods.toList.head
//        method.getParameterTypes.foreach(p => logger.debug(s"parameter name is ${p.getName}"))
        val method = locateClass(packagePrefix, name).
          getDeclaredMethod("execute", Class.forName(composeInputType(packagePrefix, inputType))) //Class.forName(composeInputType(packagePrefix, inputType))
        method.
          invoke(null, input.get).asInstanceOf[Try[Any]].getOrElse(new IllegalStateException(s"error running $name"))
      }
      case _ => ???
    }
  }

  private def composeInputType(packagePrefix: Option[String], inputType: InputType) = {
    if(inputType.name.startsWith("java.lang")) inputType.name else {
      packagePrefix.map(p => s"$p.${inputType.name}").getOrElse(inputType.name)
    }
  }

  private def locateClass(packagePrefix: Option[String], name: String) = {
    Class.forName(packagePrefix.map(prefix => s"$prefix.$name").getOrElse(name))
  }
}
