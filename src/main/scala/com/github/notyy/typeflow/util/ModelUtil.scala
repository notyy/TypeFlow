package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.{Definition, InputEndpoint, Model, OutputEndpoint, OutputType, PureFunction}
import com.typesafe.scalalogging.Logger

object ModelUtil {

  private val logger = Logger("ModelUtil")

  def definitionType(defi: Definition): String = defi.getClass.getSimpleName

  def findOutputType(instanceId: String, outputIndex: Int, model: Model): String = {
    val maybeInstance = model.activeFlow.get.instances.find(_.id == instanceId)
    (maybeInstance.map(_.definition match {
      case InputEndpoint(name, outputType) => outputType
      case PureFunction(name, input, outputs) => outputs.find(_.index == outputIndex).get.outputType
      case OutputEndpoint(name, inputType, outputType, errorOutput) => outputType
    }).map(_.name).getOrElse{
      logger.error(s"instance $instanceId not found")
      "BadType"
    })
  }
}
