package $PackageName$.aliyun

import com.aliyun.fc.runtime.{Context, HttpRequestHandler}
import com.aliyun.oss.{OSS, OSSClientBuilder}
import com.aliyuncs.fc.client.FunctionComputeClient
import com.github.notyy.typeflow.util.{JSONUtil, Param, AliyunUtil}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import scala.io.Source
import scala.util.{Failure, Success}

class $DefinitionName$Handler extends HttpRequestHandler{
  private var result: Option[Param[Object]] = None

  override def handleRequest(request: HttpServletRequest, response: HttpServletResponse, context: Context): Unit = {
    val accessKey = System.getenv("ACCESS_KEY")
    val accessSecretKey = System.getenv("SECRET_KEY")
    val accountId = System.getenv("ACCOUNT_ID")

    //TODO region should not be hardcoded
    val fcClient = new FunctionComputeClient("cn-shanghai", accountId, accessKey, accessSecretKey)
    val ossClient: OSS = new OSSClientBuilder().build("oss-cn-shanghai-internal.aliyuncs.com", accessKey, accessSecretKey)
    val source = Source.fromInputStream(request.getInputStream)
    JSONUtil.fromJSON[Param[$Params$]](source.mkString).map{ input =>
$CallingChain$
    } match {
      case Success(value) => {
        if(result.isDefined) {
          response.setStatus(200)
          response.getWriter.println(JSONUtil.toJSON(result.get))
        } else {
          val successResult = "success complete, but no result"
          response.setStatus(200)
          response.getWriter.println(successResult)
          println(successResult)
        }
      }
      case Failure(exception) => {
        response.getWriter.println(exception.getMessage)
        response.setStatus(500)
      }
    }
  }

  def setResponse(param: Param[Object]): Unit = {
    this.result = Some(param)
  }
}
