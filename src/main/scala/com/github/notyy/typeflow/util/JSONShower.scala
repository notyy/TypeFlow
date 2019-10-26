package com.github.notyy.typeflow.util

object JSONShower extends App {
  val param: Param[(Integer, Integer)] = Param(1,2)
  val json = JSONUtil.toJSON(param)
  println(json)
  val rs: Param[(Integer, Integer)] = JSONUtil.fromJSON[Param[(Integer, Integer)]](json).get
  println(rs)
}
