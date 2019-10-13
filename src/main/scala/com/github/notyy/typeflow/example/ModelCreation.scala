package com.github.notyy.typeflow.example

import com.github.notyy.typeflow.domain
import com.github.notyy.typeflow.domain._

object ModelCreation {
  val elements:Vector[Element] = Vector()
  elements.appended(
    WebInputUserInterface("用户注册界面",
      OutputTypeGoodValue("HttpUserRegisterReq"))).
    appended(
      HttpInputEndpoint("UserRegInputTrans",
        Vector(InputType("HttpUserRegisterReq")), OneOfInput,
        Vector(OutputTypeGoodValue("UserRegisterReq"), OutputTypeException("JsonTransError")), OneOfOutput)
    ).
    appended(
      DBInputEndpoint("ID生成器",
        Vector(InputType("ID类型")), OneOfInput,
        Vector(OutputTypeGoodValue("ID"), OutputTypeException("IDGenError")), OneOfOutput)
    ).
    appended(
      PureFunction("注册",
        Vector(InputType("ID"), InputType("UserRegisterReq")), AllInput,
        Vector(OutputTypeGoodValue("RegisteredUser"), OutputTypeBadValue("ValidationFailed")), OneOfOutput)
    ).appended(
    DBOutputEndPoint("保存新用户",
      Vector(InputType("RegisteredUser")),OneOfInput,
      Vector(OutputTypeGoodValue("RegisterSuccess"),OutputTypeSideEffect("RegisteredUserTable"),
        OutputTypeException("DBAccessError")),OneOfOutput)).
    appended(
      DBTable(Some("注册用户库"),InputType("RegisteredUserTable"),OutputTypeGoodValue("RegisteredUserTable"))
    )

  val model: Model = domain.Model(ModelMetaInfo("notyy"), elements)
}