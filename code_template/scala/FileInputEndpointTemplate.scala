package $PackageName$

import com.typesafe.scalalogging.Logger

import scala.io.{Source, StdIn}
import scala.util.{Failure, Success, Try}

object $DefinitionName$ extends App {
  private val logger = Logger($DefinitionName$.getClass)

  execute()

  def execute(): Unit = {
    //TODO input your file name
    val source = Source.fromFile()
    source.getLines.foreach { rawInput =>
      val input = rawInput.toInt
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
    }
    source.close()
  }
}
