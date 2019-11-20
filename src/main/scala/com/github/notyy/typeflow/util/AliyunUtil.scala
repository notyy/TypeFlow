package com.github.notyy.typeflow.util

import com.aliyuncs.fc.client.FunctionComputeClient
import com.aliyuncs.fc.request.InvokeFunctionRequest
import com.typesafe.scalalogging.Logger

import scala.util.Try

object AliyunUtil {
  private val logger = Logger("AliyunUtil")
  def callInstance(fcClient:FunctionComputeClient, params: Param[Object], serviceName: String, functionName: String): Try[Param[Object]] = {
    logger.debug(s"calling $serviceName.$functionName with parameter '$params'")
    val invkReq = new InvokeFunctionRequest(serviceName, functionName)
    val payload = JSONUtil.toJSON(params)
    invkReq.setHeader("Content-Type", "application/json");
    invkReq.setPayload(payload.getBytes)
    val invokeResp = new String(fcClient.invokeFunction(invkReq).getPayload)
    logger.debug(s"result of calling $serviceName.$functionName with parameter '$params' is '$invokeResp'")
    JSONUtil.fromJSON[Param[Object]](invokeResp)
  }
}
