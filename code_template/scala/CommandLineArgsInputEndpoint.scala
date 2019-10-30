package com.github.notyy.typeflow.tooling

import java.io.File

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, Definition}
import com.github.notyy.typeflow.editor.Model2Code.{CodeContent, CodeFileName}
import com.github.notyy.typeflow.editor.aliyun.{AliyunConfigGen, AliyunFunction, Trigger}
import com.github.notyy.typeflow.editor.{CodeLang, Model2Code, Path, PlantUML, PlantUML2Model, ReadFile, SaveToFile}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}

object CommandLineArgsInputEndpoint extends App {
  private val logger = Logger(CommandLineArgsInputEndpoint.getClass)
  if (args.length != 5) {
    println("usage: genCode {modelFilePath} {outputPath} {lang} {packageName} {platform}")
  }
  execute(args(0), args(1), args(2), args(3), args(4), args(5))

  def execute(modelFilePath: String, outputPath: String, lang: String, packageName: String, platform: String, codeUri: String): GenCodeReq = {
    ???
  }
}
