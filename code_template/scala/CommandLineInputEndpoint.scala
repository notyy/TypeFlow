package $PackageName$

import com.typesafe.scalalogging.Logger

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object $DefinitionName$ extends App {
  private val logger = Logger($DefinitionName$.getClass)

  execute()

  @scala.annotation.tailrec
  def execute(): Unit = {
    println("input an integer")
    val input = StdIn.readLine().toInt
    Try {
$CallingChain$
    } match {
      case Success(value) => {
        logger.info(s"processing input: $input successfully complete")
      }
      case Failure(exception) => {
        logger.error("error occurs", exception)
      }
    }
    execute()
  }
}
