package com.github.notyy.typeflow.editor.aliyun

object AliyunConfigGen {
  def execute(serviceName: String, functions: Vector[AliyunFunction], codeUri: String): String = {
    val template =
       s"""|ROSTemplateFormatVersion: '2015-09-01'
           |Transform: 'Aliyun::Serverless-2018-04-03'
           |Resources:
           |  $serviceName: # service name
           |    Type: 'Aliyun::Serverless::Service'
           |
           |${functionBlock(functions,codeUri)}
           |""".stripMargin
    template
  }

  def functionBlock(functions: Vector[AliyunFunction], codeUri: String): String = {
    val block = functions.map { f =>
      s"""|    ${f.name}: # function name
          |        Type: 'Aliyun::Serverless::Function'
          |        Properties:
          |          Handler: ${f.handler}::handleRequest
          |          Runtime: java8
          |          CodeUri: '$codeUri'""".stripMargin
    }.mkString(System.lineSeparator())
    block
  }
}
