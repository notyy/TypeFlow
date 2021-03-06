package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunOSSOutputEndpoint, Definition, FileOutputEndpoint, InputEndpoint, Model, OutputEndpoint, PureFunction}

object DefinitionSorter {
  def execute(model: Model): (Vector[PureFunction], Vector[InputEndpoint], Vector[OutputEndpoint], Vector[CustomType], Vector[FileOutputEndpoint], Vector[AliyunOSSOutputEndpoint]) = {
    val definitions: Vector[Definition] = model.definitions
    val inputEndpoints: Vector[InputEndpoint] = definitions.filter(_.isInstanceOf[InputEndpoint]).asInstanceOf[Vector[InputEndpoint]]
    val pureFunctions: Vector[PureFunction] = definitions.filter(_.isInstanceOf[PureFunction]).asInstanceOf[Vector[PureFunction]]
    val outputEndpoints: Vector[OutputEndpoint] = definitions.filter(_.isInstanceOf[OutputEndpoint]).asInstanceOf[Vector[OutputEndpoint]]
    val fileOutputEndpoints: Vector[FileOutputEndpoint] = definitions.filter(_.isInstanceOf[FileOutputEndpoint]).asInstanceOf[Vector[FileOutputEndpoint]]
    val aliyunOSSOutputEndpoints: Vector[AliyunOSSOutputEndpoint] = definitions.filter(_.isInstanceOf[AliyunOSSOutputEndpoint]).asInstanceOf[Vector[AliyunOSSOutputEndpoint]]
    val customTypes: Vector[CustomType] = Vector.empty
    (pureFunctions, inputEndpoints, outputEndpoints, customTypes, fileOutputEndpoints,aliyunOSSOutputEndpoints)
  }
}
