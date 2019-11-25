package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, AliyunOSSOutputEndpoint, Model, PureFunction}
import com.github.notyy.typeflow.editor.GenCodeScript.packageName

class GenPlatformHandlers(private val genAliyunHandler: GenAliyunHandler, private val genAliyunHttpInputEndpointHandler: GenAliyunHttpInputEndpointHandler, private val genAliyunOSSOutputEndpoint: GenAliyunOSSOutputEndpoint) {
  def execute(platform: Platform, aliyunHandlerCodeTemplate: CodeTemplate, aliyunHttpInputEndpointCodeTemplate: CodeTemplate,aliyunOSSOutputEndpointCodeTemplate: CodeTemplate, packageName: PackageName, model: Model): Vector[ScalaCode] = {
    val platformCodes = if (platform == Aliyun) {
      //TODO need refactoring, should AliyunOSSOutputEndpoint be put here too?
      model.definitions.map {
        case ahie: AliyunHttpInputEndpoint => genAliyunHttpInputEndpointHandler.execute(packageName, ahie, aliyunHttpInputEndpointCodeTemplate, model)
        case pureFunction: PureFunction => genAliyunHandler.execute(packageName, pureFunction, aliyunHandlerCodeTemplate)
        case aliyunOSSOutputEndpoint: AliyunOSSOutputEndpoint => genAliyunOSSOutputEndpoint.execute(packageName, aliyunOSSOutputEndpoint , aliyunOSSOutputEndpointCodeTemplate)
      }
    } else ???

    platformCodes
  }
}
