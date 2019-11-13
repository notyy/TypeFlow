package com.github.notyy.typeflow.editor

import java.io.File

import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}

class SaveCodes(private val saveToFile: SaveToFile, private val qualifiedName2CodeStructurePath: QualifiedName2CodeStructurePath) {
  private val logger = Logger(this.getClass)
  def execute(sourceCodes: Vector[SourceCode], outputPath: OutputPath): Try[Unit] = {
    val saveResult = sourceCodes.map { sc =>
      val codeLang = sc match {
        case JavaCode(_, _) => JAVA_LANG
        case ScalaCode(_, _) => SCALA_LANG
      }
      val codeStructurePath = qualifiedName2CodeStructurePath.execute(sc.qualifiedName, codeLang)
      val codeDir = QualifiedName2CodeDir.execute(sc.qualifiedName)
      mkCodeDir(CodeDir(s"${outputPath.value}/src/main/${CodeLang.str(codeLang)}/${codeDir.value}"))
      saveToFile.execute(OutputPath(s"${outputPath.value}/src/main/${CodeLang.str(codeLang)}/${codeStructurePath.value}"), sc.content)
    }
    if (saveResult.exists(_.isFailure)) {
      saveResult.filter(rs => rs.isFailure).map(_.asInstanceOf[Failure[Unit]]).reduce {
        (f1, f2) => Failure(new IllegalArgumentException(s"${f1.exception.getMessage}${System.lineSeparator()}${f2.exception.getMessage}", f1.exception))
      }
    } else {
      Success(())
    }
  }

  private def mkCodeDir(codeDirPath: CodeDir): Unit = {
    val codeDir = new File(codeDirPath.value)
    if (!codeDir.exists() || !codeDir.isDirectory) {
      logger.warn(s"create directory $codeDir")
      codeDir.mkdirs()
    }
  }
}
