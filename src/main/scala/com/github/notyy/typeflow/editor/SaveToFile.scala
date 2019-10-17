package com.github.notyy.typeflow.editor

import java.io.{File, PrintWriter}

import scala.util.Try

object SaveToFile {
  def execute(path: Path, content: String): Try[Unit] = Try {
    val file = new File(path.value)
    if (file.exists() && file.isFile) {
      file.delete()
    }
    val writer = new PrintWriter(file)
    writer.print(content)
    writer.flush()
    writer.close()
  }
}
