package com.github.notyy.typeflow.tooling

import java.io.File

import com.github.notyy.typeflow.editor.Model2Scala.{CodeContent, CodeFileName}
import com.github.notyy.typeflow.editor.{Model2Scala, Path, PlantUML, PlantUML2Model, ReadFile, SaveToFile}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success}

object GenCode extends App {
  private val logger = Logger(GenCode.getClass)
  if(args.length != 4) {
    println("usage: genCode {modelFilePath} {outputPath} {lang} {packageName}")
  }
  val modelFilePath = args(0)
  val outputPath = if(args(1).endsWith("/")) args(1).init else args(1)
  val lang: String = args(2)
  val packageName: String = args(3)

  println(s"generating source code for $modelFilePath to $outputPath")

  ReadFile.execute(Path(modelFilePath)).map { puml =>
    val model = PlantUML2Model.execute(PlantUML(modelFilePath.dropRight(5).split('/').last,puml))
    val codes: Map[CodeFileName, CodeContent] = Model2Scala.execute(model,"com.github.notyy")
    val codeDirPath = s"$outputPath/src/main/$lang/${packageName.replaceAllLiterally(".", "/")}"
    val codeDir = new File(codeDirPath)
    if (!codeDir.exists() || !codeDir.isDirectory) {
      codeDir.mkdirs()
    }
    codes.foreach{ case (codeFileName, codeContent) =>
      val codeFilePath = s"$codeDirPath/$codeFileName"
      val file = new File(codeFilePath)
      if(file.exists() && file.isFile) {
        println(s"$codeFilePath already exist,skipped")
      }else {
        SaveToFile.execute(Path(codeFilePath), codeContent) match {
          case Success(_) => println(s"$codeFilePath generated")
          case Failure(exception) => logger.error(s"error when saving file to $codeFilePath", exception)
        }
      }
    }
  } match {
    case Success(_) => println("code generation successfully completed")
    case Failure(exception) => {
      println(s"code generation failed, error message is ${exception.getMessage}")
      logger.error(s"code generation failed for $modelFilePath to $outputPath", exception)
    }
  }
}
