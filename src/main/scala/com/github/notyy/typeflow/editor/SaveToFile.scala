package com.github.notyy.typeflow.editor

import java.io.{File, PrintWriter}
import com.typesafe.scalalogging.Logger
import scala.util.Try

class SaveToFile {
  private val logger = Logger(this.getClass)

  def execute(path: Path, content: String): Try[Unit] = Try {
    logger.info(s"saving file to ${path.value}")
    val file = new File(path.value)
    if (file.exists() && file.isFile) {
      logger.warn(s"file ${path.value} already exist, won't overwrite")
    } else {
      val writer = new PrintWriter(file)
      writer.print(content)
      writer.flush()
      writer.close()
    }
  }
}
