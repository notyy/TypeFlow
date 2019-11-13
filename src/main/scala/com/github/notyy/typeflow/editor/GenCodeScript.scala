package com.github.notyy.typeflow.editor

import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}

object GenCodeScript extends App {
  private val logger = Logger(GenCodeScript.getClass)
  if (args.length != 5) {
    println("usage: genCode {modelFilePath} {outputPath} {lang} {packageName} {platform}")
  }
  val (modelPath, codeLang, outputPath, packageName) = execute(args(0), args(1), args(2), args(3), args(4), args(5))
  val result = ReadFile.execute(modelPath).map { puml =>
    val model = PlantUML2Model.execute(ModelPath2ModelName.execute(modelPath.value), puml)
    val (pureFunctions, inputEndpoints, outputEndpoints, customTypes) = DefinitionSorter.execute(model)
    val genPureFunctions = new GenPureFunctions(new GenJavaPureFunction(new GenFormalParams))
    val genCommandLineInputEndpoints = new GenCommandLineInputEndpoints(new GenCommandLineInputEndpoint)
    val genOutputEndpoints = new GenOutputEndpoints(new GenJavaOutputEndpoint(new GenFormalParams))
    val saveCodes = new SaveCodes(new SaveToFile, new QualifiedName2CodeStructurePath)

    val pureFunctionSaveRs = LoadPureFunctionCodeTemplate.execute(JAVA_LANG).flatMap { javaPureFunctionCodeTemplate =>
      val pureFunctionCodes = genPureFunctions.execute(codeLang, pureFunctions, packageName, javaPureFunctionCodeTemplate)
      saveCodes.execute(pureFunctionCodes, outputPath)
    }

    val (commandLineArgsInputEndpoints, commandLineInputEndpoints, aliyunHttpInputEndpoints) = InputEndpointSorter.execute(inputEndpoints)
    val commandLineInputEndpointSaveRs = LoadInputEndpointCodeTemplate.execute(SCALA_LANG).flatMap { scalaCommandLineInputEndpointCodeTemplate =>
      val commandLineInputEndpointCodes = genCommandLineInputEndpoints.execute(commandLineInputEndpoints, packageName, scalaCommandLineInputEndpointCodeTemplate, model)
      saveCodes.execute(commandLineInputEndpointCodes, outputPath)
    }

    val outputEndpointSaveRs = LoadOutputEndpointCodeTemplate.execute(JAVA_LANG).flatMap { javaOutputEndpointCodeTemplate =>
      val outputEndpointCodes = genOutputEndpoints.execute(JAVA_LANG, outputEndpoints, packageName, javaOutputEndpointCodeTemplate)
      saveCodes.execute(outputEndpointCodes, outputPath)
    }

    val totalRs: Vector[Try[Unit]] = Vector(pureFunctionSaveRs, commandLineInputEndpointSaveRs, outputEndpointSaveRs)
    if (totalRs.exists(_.isFailure)) {
      totalRs.filter(_.isFailure).map(_.asInstanceOf[Failure[Unit]]).reduce {
        (f1, f2) => Failure(new IllegalArgumentException(s"${f1.exception.getMessage}${System.lineSeparator()}${f2.exception.getMessage}", f1.exception))
      }
    } else {
      Success(())
    }
  }

  result match {
    case Success(_) => {
      println("code generation successfully")
    }
    case Failure(exception) => {
      logger.error(s"error when generating code for ${modelPath.value} ", exception)
    }
  }

  def execute(modelFilePath: String, outputPath: String, lang: String, packageName: String, platform: String, codeUri: String): (ModelFilePath, CodeLang, OutputPath, PackageName) = {
    (ModelFilePath(modelFilePath), CodeLang.from(lang), OutputPath(outputPath), PackageName(packageName))
  }
}
