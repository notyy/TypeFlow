package com.github.notyy.typeflow.editor

import java.io.{File, PrintWriter}

import scala.util.Try

object SaveToFile {

  case class SaveFileReq(path: String, content: String)

  def execute(saveFileReq: SaveFileReq): Try[Unit] = Try {
    val file = new File(saveFileReq.path)
    if (file.exists() && file.isFile) {
      file.delete()
    }
    val writer = new PrintWriter(file)
    writer.print(saveFileReq.content)
    writer.flush()
  }
}
