package com.github.notyy.typeflow.editor.aliyun

import java.io.{InputStream, OutputStream}

import com.aliyun.fc.runtime.{Context, StreamRequestHandler}
import com.github.notyy.typeflow.editor.{UserInput, UserInputInterpreter}
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats}

import scala.io.Source

class UserInputInterpreterHandler extends StreamRequestHandler{
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val inStr = Source.fromInputStream(input).mkString
    val rs = UserInputInterpreter.execute(UserInput(inStr))
    output.write(JSONUtil.toJSON(rs,JSonFormats.userInterpreterResultFormats).getBytes)
  }
}
