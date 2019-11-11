package $packageName$

import com.github.notyy.typeflow.editor.{LocalRunEngine, Path}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object $DefinitionName$ extends App {
  private val logger = Logger($DefinitionName$.getClass)

  execute()

  @scala.annotation.tailrec
  def execute(): Unit = {
    println("input an integer")
    Try {
      val input = StdIn.readLine().toInt
      $CallingChain$
    } match {
      case Success(value) => ()
      case Failure(exception) => {
        logger.error("error occurs", exception)
      }
    }
    execute()
  }
}
