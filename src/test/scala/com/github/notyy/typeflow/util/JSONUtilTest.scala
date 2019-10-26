package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.InputType
import com.github.notyy.typeflow.editor.{CreateModelCommand, InterpreterResult, UnknownCommand}
import org.json4s.{DefaultFormats, Formats, ShortTypeHints}
import org.scalatest.{FunSpec, Matchers}

import scala.util.{Failure, Success}

class JSONUtilTest extends FunSpec with Matchers {
  case class User(name: String ,age: Int)

  describe("JSONUtil"){
    it("can toJson and fromJson on a polymorphic scala type, with a customized format with type hints"){
      val formats: Formats = DefaultFormats.withHints(ShortTypeHints(List(classOf[UnknownCommand], classOf[CreateModelCommand])))
      val json = JSONUtil.toJSON(UnknownCommand("unknown"),formats)
      println(json)
      val optionRs = JSONUtil.fromJSON[InterpreterResult](json,formats)
      val outStr = optionRs match {
        case Success(UnknownCommand(input)) => s"unknown command $input"
        case Failure(exception) => {
          exception.printStackTrace()
          s"what's this $optionRs"
        }
      }
      println(outStr)
      outStr shouldBe "unknown command unknown"
    }
    it("can format simple type with default format") {
      val expectedJson = """{"name":"xyz","age":10}"""
      JSONUtil.toJSON(User("xyz", 10)) shouldBe expectedJson
      val user = JSONUtil.fromJSON[User](expectedJson).get
      user.name shouldBe "xyz"
      user.age shouldBe 10
    }
    it("this test used to manually show json") {
      val value:Integer = 1
      println(s"json of $value is: ${JSONUtil.toJSON(value)}")
    }
  }
}
