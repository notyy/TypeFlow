package com.github.notyy.typeflow.editor

import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}

object GenCodeScript extends App {
  private val logger = Logger(GenCodeScript.getClass)
  if (args.length != 5) {
    println("usage: genCode {modelFilePath} {outputPath} {lang} {packageName} {platform}")
  }
  val (modelPath,codeLang, outputPath, packageName) = execute(args(0), args(1), args(2), args(3), args(4), args(5))
  val readFileRs = ReadFile.execute(modelPath)
  readFileRs match {
    case Success(puml) => {
      val model = PlantUML2Model.execute(ModelPath2ModelName.execute(modelPath.value),puml)
    }
    case Failure(exception) => ???
  }

  def execute(modelFilePath: String, outputPath: String, lang: String, packageName: String, platform: String, codeUri: String): (Path, CodeLang, Path, PackageName) = {
    (Path(modelFilePath),CodeLang.from(lang),Path(outputPath), PackageName(packageName))
  }
}
