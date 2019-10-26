package com.github.notyy.typeflow.editor.aliyun

import com.aliyun.oss.OSS

import scala.io.Source
import scala.util.Try

//TODO consider put these util functions also onto aliyun, under a specific namespace: typeflow_
class ReadFileFromAliyunOSS(val ossClient: OSS) {
  def execute(bucketName: String, objectName: String): Try[String] = Try {
    val source = Source.fromInputStream(ossClient.getObject(bucketName, objectName).getObjectContent)
    val content = source.mkString
    source.close()
    content
  }
}

object ReadFileFromAliyunOSS {
  def apply(ossClient: OSS): ReadFileFromAliyunOSS = {
    new ReadFileFromAliyunOSS(ossClient)
  }
}
