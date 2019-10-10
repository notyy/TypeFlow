package com.notyy.typeflow.example1

import scala.io._
import java.io._

object CommandRecorder {
  val logFile = new File("./localOutput/log.txt")
  if (logFile.exists() && logFile.isFile) {
    logFile.delete()
  }
  logFile.createNewFile()

  val writer = new PrintWriter(logFile)

  def execute(command: String): Unit = {
    writer.println(command)
    writer.flush()
  }
}
