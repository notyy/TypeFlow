package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, CommandLineInputEndpoint, Definition, InputEndpoint, Model, OutputEndpoint, OutputType, PureFunction}
import com.typesafe.scalalogging.Logger

object ModelUtil {

  private val logger = Logger("ModelUtil")

  def definitionType(defi: Definition): String = defi.getClass.getSimpleName

  def findOutputTypeRemovePrefixShortName(instanceId: String, outputIndex: Int, model: Model): Option[String] = {
    findOutputTypeRemovePrefix(instanceId,outputIndex,model).map(_.split('.').last)
  }

  def findOutputTypeRemovePrefix(instanceId: String, outputIndex: Int, model: Model): Option[String] = {
    findOutputType(instanceId, outputIndex, model).map(removePrefix)
  }

  def findOutputType(instanceId: String, outputIndex: Int, model: Model) = {
    val maybeInstance = model.activeFlow.get.instances.find(_.id == instanceId)
    maybeInstance.flatMap(_.definition match {
      case CommandLineInputEndpoint(name, outputType) => Some(outputType)
      case AliyunHttpInputEndpoint(name, outputType) => Some(outputType)
      case PureFunction(name, input, outputs) => outputs.find(_.index == outputIndex).map(_.outputType)
      case OutputEndpoint(name, inputType, outputType, errorOutput) => Some(outputType)
    }).map(_.name)
  }

  def removePrefix(outputName: String): String = outputName.split("::").last
}
