package $packageName$.aliyun

import java.io.{InputStream, OutputStream}

import com.aliyun.fc.runtime.{Context, StreamRequestHandler}
import com.github.notyy.$TypeFlowFunction$
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}

import scala.io.Source

class $TypeFlowFunction$Handler extends StreamRequestHandler {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val inStr = Source.fromInputStream(input).mkString
    JSONUtil.fromJSON[$params$](inStr).map { param =>
      val rs = $TypeFlowFunction$.execute($paramCall$)
      $writeOutput$
    }
  }
}
