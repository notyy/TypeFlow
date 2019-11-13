package com.github.notyy.typeflow.editor

import scala.util.{Failure, Success, Try}

class SaveCodes(private val saveToFile: SaveToFile, private val qualifiedName2CodeStructurePath: QualifiedName2CodeStructurePath) {
  def execute(sourceCodes: Vector[SourceCode], outputPath: OutputPath): Try[Unit] = {
    val saveResult = sourceCodes.map { sc =>
      val codeLang = sc match {
        case JavaCode(_, _) => JAVA_LANG
        case ScalaCode(_, _) => SCALA_LANG
      }
      val codeStructurePath = qualifiedName2CodeStructurePath.execute(sc.qualifiedName, codeLang)
      saveToFile.execute(OutputPath(s"$outputPath/${codeStructurePath.value}"), sc.content)
    }
    if (saveResult.exists(_.isFailure)) {
      saveResult.filter(rs => rs.isFailure).map(_.asInstanceOf[Failure[Unit]]).reduce {
         (f1, f2) => Failure(new IllegalArgumentException(s"${f1.exception.getMessage}${System.lineSeparator()}${f2.exception.getMessage}", f1.exception))
      }
    } else {
      Success(())
    }
  }
}
