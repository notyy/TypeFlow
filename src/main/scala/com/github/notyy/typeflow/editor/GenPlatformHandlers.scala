package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, Model, PureFunction}

class GenPlatformHandlers(private val genAliyunHandler: GenAliyunHandler, private val genAliyunHttpInputEndpointHandler: GenAliyunHttpInputEndpointHandler) {
  def execute(platform: Platform, aliyunHandlerCodeTemplate: CodeTemplate,aliyunHttpInputEndpointCodeTemplate: CodeTemplate, packageName: PackageName, model: Model): Vector[ScalaCode] = {
    val platformCodes = if(platform == Aliyun) {
      model.definitions.map {
        case ahie: AliyunHttpInputEndpoint => genAliyunHttpInputEndpointHandler.execute(packageName,ahie,aliyunHttpInputEndpointCodeTemplate)
        case defi => genAliyunHandler.execute(packageName,defi,aliyunHandlerCodeTemplate)
      }
    }else ???

    platformCodes
  }
}
