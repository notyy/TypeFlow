package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, CommandLineArgsInputEndpoint, CommandLineInputEndpoint, FileInputEndpoint, InputEndpoint}

object InputEndpointSorter {
  def execute(inputEndpoints: Vector[InputEndpoint]): (Vector[CommandLineArgsInputEndpoint], Vector[CommandLineInputEndpoint], Vector[AliyunHttpInputEndpoint], Vector[FileInputEndpoint]) = {
    val commandLineArgsInputEndpoints = inputEndpoints.filter(_.subName == "CommandLineArgsInputEndpoint").asInstanceOf[Vector[CommandLineArgsInputEndpoint]]
    val commandLineInputEndpoints = inputEndpoints.filter(_.subName == "CommandLineInputEndpoint").asInstanceOf[Vector[CommandLineInputEndpoint]]
    val aliyunHttpInputEndpoints = inputEndpoints.filter(_.subName == "AliyunHttpInputEndpoint").asInstanceOf[Vector[AliyunHttpInputEndpoint]]
    val fileInputEndpoints = inputEndpoints.filter(_.subName == "FileInputEndpoint").asInstanceOf[Vector[FileInputEndpoint]]
    (commandLineArgsInputEndpoints, commandLineInputEndpoints, aliyunHttpInputEndpoints, fileInputEndpoints)
  }
}
