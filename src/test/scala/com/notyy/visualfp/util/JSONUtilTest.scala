package com.notyy.visualfp.util

import com.notyy.visualfp.example1.UserInputInterpreter
import com.notyy.visualfp.example1.UserInputInterpreter.{CreateModelCommand, InterpreterResult, UnknownCommand}
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Formats, ShortTypeHints}
import org.scalatest.{FunSpec, Matchers}

import scala.util.{Failure, Success, Try}

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
  }
}
