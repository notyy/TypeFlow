package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain._
import com.typesafe.scalalogging.Logger

import scala.util.Try

object ReflectRunner {
  private val logger = Logger("ReflectRunner")

  def run(definition: Definition, packagePrefix: Option[String], inputParams: Option[Vector[Any]]): Any = {
    logger.debug(s"input.getClass is ${inputParams.map(_.map(_.getClass.getName).mkString(",")).getOrElse("no argument")}")
    definition match {
      case PureFunction(name, inputs, _) => {
        logger.debug(s"running PureFuction $name who's input types are $inputs")
        val clazz = locateClass(packagePrefix, name)
        logger.debug(s"class located for $packagePrefix.$name")
        val method = clazz.
          getDeclaredMethod("execute", inputs.map(input => Class.forName(TypeUtil.composeInputType(packagePrefix, input.inputType))):_*)
        val invokeResult = method.invoke(clazz.newInstance(), inputParams.get: _*)
        logger.debug(s"result of calling $name with $inputParams is $invokeResult")
        invokeResult match {
          case value: Try[Any] =>
            value.getOrElse(new IllegalStateException(s"error running $name"))
          case _ =>
            invokeResult
        }
      }
      case CommandLineInputEndpoint(name, output) => {
        logger.debug(s"running CommandLineInputEndpoint $name")
        runInputEndpoint(packagePrefix, name)
      }
      case OutputEndpoint(name, inputs, outputType, errorOutput) => {
        logger.debug(s"running OutputEndpoint $name who's input types are $inputs")
        val clazz = locateClass(packagePrefix, name)
        logger.debug(s"class located for $packagePrefix.$name")
        val method = clazz.
        getDeclaredMethod("execute", inputs.map(input => Class.forName(TypeUtil.composeInputType(packagePrefix, input.inputType))):_*) //Class.forName(composeInputType(packagePrefix, inputType))
        logger.debug(s"method located for $packagePrefix.$name.execute")

        try {
          val invokeResult = method.invoke(clazz.newInstance(), inputParams.get: _*)
          logger.debug(s"invoke result is $invokeResult")
          invokeResult match {
            case value: Try[Any] =>
              value.getOrElse(new IllegalStateException(s"error running $name"))
            case _ =>
              invokeResult
          }
        }catch{
          case ex:Exception => ex.printStackTrace()
        }
      }
      case _ => {
        logger.error(s"what's this? $definition")
        ???
      }
    }
  }

  def runInputEndpoint(packagePrefix: Option[String], name: String): AnyRef = {
    locateClass(packagePrefix, name).
      getDeclaredMethod("execute").invoke(null)
  }

  private def locateClass(packagePrefix: Option[String], name: String) = {
    Class.forName(packagePrefix.map(prefix => s"$prefix.$name").getOrElse(name))
  }
}
