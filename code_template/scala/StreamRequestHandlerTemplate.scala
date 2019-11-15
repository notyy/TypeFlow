package $PackageName$.aliyun

import java.io.{InputStream, OutputStream}

import com.aliyun.fc.runtime.{Context, StreamRequestHandler}
import com.github.notyy.$DefinitionName$
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats, Param}

import scala.io.Source
import scala.util.{Failure, Success}

class $DefinitionName$Handler extends StreamRequestHandler {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val inStr = Source.fromInputStream(input).mkString
    JSONUtil.fromJSON[Param[$params$]](inStr).map { param =>
      $Callee$.execute($paramCall$)
    } match {
      case Success(value) => $writeOutput$
      case Failure(exception) => output.write(exception.getMessage.getBytes)
    }
  }
}
