package $PackageName$.aliyun

import java.io.{ByteArrayInputStream, InputStream, OutputStream}

import com.aliyun.fc.runtime.{Context, StreamRequestHandler}
import com.aliyun.oss.OSSClientBuilder
import com.github.notyy.typeflow.util.{JSONUtil, Param}

import scala.io.Source
import scala.util.{Failure, Success, Try}

class $DefinitionName$Handler extends StreamRequestHandler {
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val accessKey = System.getenv("ACCESS_KEY")
    val accessSecretKey = System.getenv("SECRET_KEY")
    val accountId = System.getenv("ACCOUNT_ID")
    val bucketName = System.getenv("BUCKET_NAME")
    val objectName = System.getenv("OBJECT_NAME")

    Try {
      val ossClient = new OSSClientBuilder().build("oss-cn-shanghai-internal.aliyuncs.com", accessKey, accessSecretKey)
      val inStr = Source.fromInputStream(input).mkString
      ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(inStr.getBytes))
    } match {
      case Success(value) => $WriteOutput$
      case Failure(exception) => output.write(exception.getMessage.getBytes)
    }
  }
}
