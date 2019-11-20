package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, Definition}
import com.github.notyy.typeflow.editor.aliyun.{AliyunConfigGen, AliyunFunction, Trigger}
import com.typesafe.scalalogging.Logger

object GenAliyunTemplate {
  private val logger = Logger("GenAliyunTemplate")

  def execute(serviceName: String, definitions: Vector[Definition], codeUri: CodeUri, packageName: PackageName, outputPath: OutputPath): Unit = {
    val functions: Vector[AliyunFunction] = definitions.map { defi =>
      defi match {
        case AliyunHttpInputEndpoint(name, ot) => AliyunFunction(name, s"${packageName.value}.aliyun.${name}Handler", Some(Trigger(s"$name-http-trigger", "HTTP")))
        case _ => AliyunFunction(defi.name, s"${packageName.value}.aliyun.${defi.name}Handler", None)
      }
    }
    val yml = AliyunConfigGen.execute(serviceName, functions, codeUri)
    val aliyunYMLPath = s"${outputPath.value}/template.yml"
    new SaveToFile().execute(OutputPath(aliyunYMLPath), Content(yml))
    logger.debug(s"aliyun config file saved to $aliyunYMLPath")
  }
}
