package com.github.notyy.typeflow.editor

class SaveCodes(val saveToFile: SaveToFile, val qualifiedName2CodeStructurePath: QualifiedName2CodeStructurePath) {
  def execute(sourceCodes: Vector[SourceCode], outputPath: OutputPath): Unit = {
    sourceCodes.foreach { sc =>
//      saveToFile.execute()
    }
  }
}
