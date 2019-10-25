package $packageName$

import com.github.notyy.typeflow.editor.{LocalRunEngine, Path}
import com.github.notyy.typeflow.util.TypeUtil
import com.typesafe.scalalogging.Logger

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object $InputEndpointName$ extends App {
  private val logger = Logger($InputEndpointName$.getClass)

  val modelFilePath = args(0)
  logger.debug(s"running flow $modelFilePath")
  val packageName = this.getClass.getPackage.getName
  val inputEndpointName = TypeUtil.getTypeShortName(this)
  logger.debug(s"inputEndpoint is $packageName.$inputEndpointName")

  execute()

  @scala.annotation.tailrec
  def execute(): Unit = {
    println("input an integer")
    Try {
      val output = StdIn.readLine().toInt
      LocalRunEngine.runFlow(Path(modelFilePath), inputEndpointName, packageName, output)
    } match {
      case Success(value) => ()
      case Failure(exception) => {
        println("can only accept integer")
      }
    }
    execute()
  }
}
