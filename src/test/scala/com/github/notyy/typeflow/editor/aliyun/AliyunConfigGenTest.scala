package com.github.notyy.typeflow.editor.aliyun

import com.typesafe.scalalogging.Logger
import org.scalatest.{FunSpec, Matchers}

class AliyunConfigGenTest extends FunSpec with Matchers {
  private val logger = Logger(this.getClass)
  describe("AliyunConfigGen") {
    it("can generate template.yml for aliyun tool:fun") {
      val functions = Vector(
        AliyunFunction("A", "com.github.notyy.aliyun.AHandler", None),
        AliyunFunction("B", "com.github.notyy.aliyun.BHandler", None),
        AliyunFunction("C", "com.github.notyy.aliyun.CHandler", Some(Trigger("C-http-trigger","HTTP")))
      )
      val yml = AliyunConfigGen.execute("SampleService", functions, "oss://type-flow/type-flow-assembly-0.0.1.jar")
      logger.debug(yml)
      yml shouldBe
        """|ROSTemplateFormatVersion: '2015-09-01'
           |Transform: 'Aliyun::Serverless-2018-04-03'
           |Resources:
           |  SampleService: # service name
           |    Type: 'Aliyun::Serverless::Service'
           |
           |    A: # function name
           |        Type: 'Aliyun::Serverless::Function'
           |        Properties:
           |          Handler: com.github.notyy.aliyun.AHandler::handleRequest
           |          Runtime: java8
           |          CodeUri: 'oss://type-flow/type-flow-assembly-0.0.1.jar'
           |
           |
           |    B: # function name
           |        Type: 'Aliyun::Serverless::Function'
           |        Properties:
           |          Handler: com.github.notyy.aliyun.BHandler::handleRequest
           |          Runtime: java8
           |          CodeUri: 'oss://type-flow/type-flow-assembly-0.0.1.jar'
           |
           |
           |    C: # function name
           |        Type: 'Aliyun::Serverless::Function'
           |        Properties:
           |          Handler: com.github.notyy.aliyun.CHandler::handleRequest
           |          Runtime: java8
           |          CodeUri: 'oss://type-flow/type-flow-assembly-0.0.1.jar'
           |        Events:
           |          C-http-trigger: # trigger name
           |            Type: HTTP # http trigger
           |            Properties:
           |              AuthType: ANONYMOUS
           |              Methods: ['GET', 'POST', 'PUT']
           |
           |
           |""".stripMargin
    }
  }
}
