package com.github.notyy.typeflow.editor

import scala.util.{Failure, Success, Try}

class SaveCodes(val saveToFile: SaveToFile, val qualifiedName2CodeStructurePath: QualifiedName2CodeStructurePath) {
  def execute(sourceCodes: Vector[SourceCode], outputPath: OutputPath): Try[Unit] = {
    val saveResult = sourceCodes.map { sc =>
      val codeLang = sc match {
        case JavaCode(_,_) => JAVA_LANG
        case ScalaCode(_,_) => SCALA_LANG
      }
      val codeStructurePath = qualifiedName2CodeStructurePath.execute(sc.qualifiedName, codeLang)
      saveToFile.execute(OutputPath(s"$outputPath/${codeStructurePath.value}"), sc.content)
    }
    if(saveResult.exists(_.isFailure)) {
      saveResult.filter(_.isFailure).reduce {
        case (Failure(e1), Failure(e2)) => Failure(new IllegalArgumentException(s"${e1.getMessage}${System.lineSeparator()}${e2.getMessage}", e1))
      }
    }else {
      Success(())
    }
  }
}
