package com.github.notyy.typeflow.editor.aliyun

import com.aliyun.fc.runtime.{Context, HttpRequestHandler}
import com.aliyuncs.fc.client.FunctionComputeClient
import com.aliyuncs.fc.request.InvokeFunctionRequest
import com.github.notyy.typeflow.editor.UserInputInterpreter
import com.github.notyy.typeflow.editor.UserInputInterpreter.UnknownCommand
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import scala.util.Success

class HttpInputEndpoint extends HttpRequestHandler{
  override def handleRequest(request: HttpServletRequest, response: HttpServletResponse, context: Context): Unit = {
    val accessKey = System.getenv("ACCESS_KEY")
    val accessSecretKey = System.getenv("SECRET_KEY")
    val accountId = System.getenv("ACCOUNT_ID")

    val fcClient = new FunctionComputeClient("cn-shanghai", accountId, accessKey, accessSecretKey)
    val invkReq = new InvokeFunctionRequest("TypeFlow", "UserInputInterpreter")
    val payload = "unknown"
    //        invkReq.setHeader("Content-Type", "application/json");
    invkReq.setPayload(payload.getBytes)
    val invokeResp = new String(fcClient.invokeFunction(invkReq).getPayload)
    val rs = JSONUtil.fromJSON[UserInputInterpreter.InterpreterResult](invokeResp,JSonFormats.userInterpreterResultFormats)
    val outStr = rs match {
      case Success(UnknownCommand(input)) => s"unknown command $input"
      case _ => s"what's this $rs"
    }
    response.getWriter.println(outStr)
    response.setStatus(200)
  }
}
