package com.github.notyy.typeflow.util

import org.json4s._
import org.json4s.native.Serialization

import scala.util.Try

object JSONUtil {
//  implicit val formats = DefaultFormats
  def toJSON(objectToWrite: AnyRef, formats: Formats = DefaultFormats): String = Serialization.write(objectToWrite)(formats)

  def fromJSON[T](jsonString: String, formats: Formats = DefaultFormats)(implicit mf: Manifest[T]): Try[T] = {
    implicit val internalFormats:Formats = formats
    Try(Serialization.read(jsonString))
  }

}
