package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain.{AliyunHttpInputEndpoint, CommandLineArgsInputEndpoint, CommandLineInputEndpoint, InputEndpoint, OutputType}
import org.scalatest.{FunSpec, Matchers}

class InputEndpointSorterTest extends FunSpec with Matchers {
  describe("InputEndpointSorter") {
    it("will divide input end points by type") {
      val inputs: Vector[InputEndpoint] =
        Vector(
          CommandLineArgsInputEndpoint("cmlargs1", OutputType("Integer")),
          CommandLineInputEndpoint("numInput", OutputType("Integer")),
          AliyunHttpInputEndpoint("aliyunNumInput", OutputType("Integer"))
        )
      val (cmlargs, numInputs, aliyunNumInputs) = InputEndpointSorter.execute(inputs)
      cmlargs.length shouldBe 1
      cmlargs.exists(_.name == "cmlargs1") shouldBe true
      numInputs.length shouldBe 1
      numInputs.exists(_.name == "numInput") shouldBe true
      aliyunNumInputs.length shouldBe 1
      aliyunNumInputs.exists(_.name == "aliyunNumInput") shouldBe true
    }
  }
}
