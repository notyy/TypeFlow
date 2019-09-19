package com.notyy.visualfp.example

import com.notyy.visualfp.domain.{AllInput, Connection, DBInputEndpoint, DBOutputEndPoint, DBTable, Element, HttpInputEndpoint, InputType, Model, ModelMetaInfo, OneOfInput, OneOfOutput, OutputTypeBadValue, OutputTypeException, OutputTypeGoodValue, OutputTypeSideEffect, PureFunction, WebInputUserInterface}
import com.notyy.visualfp.domain.Common._

object ModelCreation {
  val elements = Vector[Element]()
  elements.appended(
    WebInputUserInterface(Some("用户注册界面"),
      OutputTypeGoodValue("HttpUserRegisterReq"))).
    appended(
      HttpInputEndpoint(Some("UserRegInputTrans"),
        Vector(InputType("HttpUserRegisterReq")), OneOfInput,
        Vector(OutputTypeGoodValue("UserRegisterReq"), OutputTypeException("JsonTransError")), OneOfOutput)
    ).
    appended(
      DBInputEndpoint(Some("ID生成器"),
        Vector(InputType("ID类型")), OneOfInput,
        Vector(OutputTypeGoodValue("ID"), OutputTypeException("IDGenError")), OneOfOutput)
    ).
    appended(
      PureFunction(Some("注册"),
        Vector(InputType("ID"), InputType("UserRegisterReq")), AllInput,
        Vector(OutputTypeGoodValue("RegisteredUser"), OutputTypeBadValue("ValidationFailed")), OneOfOutput)
    ).appended(
    DBOutputEndPoint(Some("保存新用户"),
      Vector(InputType("RegisteredUser")),OneOfInput,
      Vector(OutputTypeGoodValue("RegisterSuccess"),OutputTypeSideEffect("RegisteredUserTable"),
        OutputTypeException("DBAccessError")),OneOfOutput)).
    appended(
      DBTable(Some("注册用户库"),InputType("RegisteredUserTable"),OutputTypeGoodValue("RegisteredUserTable"))
    )

  val model: Model = Model(ModelMetaInfo("notyy"), elements)
}
