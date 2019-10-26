package $packageName$.aliyun

import com.aliyun.fc.runtime.{Context, HttpRequestHandler}
import com.aliyun.oss.{OSS, OSSClientBuilder}
import com.aliyuncs.fc.client.FunctionComputeClient
import com.github.notyy.typeflow.editor.aliyun.AliyunRunEngine
import com.github.notyy.typeflow.util.{JSONUtil, Param}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import scala.io.Source
import scala.util.{Failure, Success}

class $InputEndpointName$Handler extends HttpRequestHandler{
  override def handleRequest(request: HttpServletRequest, response: HttpServletResponse, context: Context): Unit = {
    val accessKey = System.getenv("ACCESS_KEY")
    val accessSecretKey = System.getenv("SECRET_KEY")
    val accountId = System.getenv("ACCOUNT_ID")

    //TODO region should not be hardcoded
    val fcClient = new FunctionComputeClient("cn-shanghai", accountId, accessKey, accessSecretKey)
    val ossClient: OSS = new OSSClientBuilder().build("oss-cn-shanghai-internal.aliyuncs.com", accessKey, accessSecretKey)
    val source = Source.fromInputStream(request.getInputStream)
    JSONUtil.fromJSON[Param[$OutputType$]](source.mkString).flatMap{ output =>
      AliyunRunEngine.runFlow("$bucketName$", "$objectName$", "$InputEndpointName$", output.asInstanceOf[Param[Object]],fcClient,ossClient)
    } match {
      case Success(value) => {
        val successResult = "success complete"
        println(successResult)
        response.getWriter.println(successResult)
        response.setStatus(200)
      }
      case Failure(exception) => {
        response.getWriter.println(exception.getMessage)
        response.setStatus(500)
      }
    }
  }
}
