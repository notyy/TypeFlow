package com.github.notyy.typeflow.tooling

import java.io.File

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, Definition}
import com.github.notyy.typeflow.editor.Model2Code.{CodeContent, CodeFileName}
import com.github.notyy.typeflow.editor.aliyun.{AliyunConfigGen, AliyunFunction, Trigger}
import com.github.notyy.typeflow.editor.{CodeLang, Model2Code, Path, PlantUML, PlantUML2Model, ReadFile, SaveToFile}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}

object GenCode extends App {
  private val logger = Logger(GenCode.getClass)
  if (args.length != 5) {
    println("usage: genCode {modelFilePath} {outputPath} {lang} {packageName} {platform}")
  }
  val modelFilePath = args(0)
  val outputPath = if (args(1).endsWith("/")) args(1).init else args(1)
  val lang: String = args(2)
  val codeLang = CodeLang.from(lang)
  val packageName: String = args(3)
  val platform: String = args(4)
  val codeUri: String = args(5)

  if (platform != Model2Code.PLATFORM_LOCAL && platform != Model2Code.PLATFORM_ALIYUN) {
    println(s"unknown platform $platform")
    System.exit(1)
  }

  println(s"generating source code for $modelFilePath to $outputPath")

  ReadFile.execute(Path(modelFilePath)).map { puml =>
    val model = PlantUML2Model.execute(PlantUML(modelFilePath.dropRight(5).split('/').last, puml))
    val codes: Map[CodeFileName, CodeContent] = Model2Code.execute(model, "com.github.notyy", platform, codeLang)
    logger.debug(s"totally ${codes.size} code files to be generated")
    val codeDirPath = s"$outputPath/src/main/$lang/${packageName.replaceAllLiterally(".", "/")}"
    //for now, platform codes are always scala
    val scalaCodeDirPath = s"$outputPath/src/main/scala/${packageName.replaceAllLiterally(".", "/")}"
    mkCodeDir(codeDirPath)
    if (platform == Model2Code.PLATFORM_ALIYUN) {
      mkCodeDir(s"$scalaCodeDirPath/aliyun")
    }
    codes.foreach { case (codeFileName, codeContent) =>
      val codeFilePath = if(codeFileName.endsWith(".scala")) s"$scalaCodeDirPath/$codeFileName" else s"$codeDirPath/$codeFileName"
      val file = new File(codeFilePath)
      if (file.exists() && file.isFile) {
        println(s"$codeFilePath already exist,skipped")
      } else {
        SaveToFile.execute(Path(codeFilePath), codeContent) match {
          case Success(_) => println(s"$codeFilePath generated")
          case Failure(exception) => logger.error(s"error when saving file to $codeFilePath", exception)
        }
      }
    }
    genAliyunTemplate(model.name, model.definitions, codeUri)
  } match {
    case Success(_) => println("code generation successfully completed")
    case Failure(exception) => {
      println(s"code generation failed, error message is ${exception.getMessage}")
      logger.error(s"code generation failed for $modelFilePath to $outputPath", exception)
    }
  }

  private def genAliyunTemplate(serviceName: String, definitions: Vector[Definition], codeUri: String): Unit = {
    val functions: Vector[AliyunFunction] = definitions.map { defi =>
      defi match {
        case AliyunHttpInputEndpoint(name,ot) => AliyunFunction(name, s"$packageName.aliyun.${name}Handler", Some(Trigger(s"$name-http-trigger","HTTP")))
        case _ => AliyunFunction(defi.name, s"$packageName.aliyun.${defi.name}Handler", None)
      }
    }
    val yml = AliyunConfigGen.execute(serviceName, functions, codeUri)
    val aliyunYMLPath = s"$outputPath/template.yml"
    SaveToFile.execute(Path(aliyunYMLPath), yml)
    logger.debug(s"aliyun config file saved to $aliyunYMLPath")
  }

  private def mkCodeDir(codeDirPath: String): Unit = {
    val codeDir = new File(codeDirPath)
    if (!codeDir.exists() || !codeDir.isDirectory) {
      codeDir.mkdirs()
    }
  }
}
