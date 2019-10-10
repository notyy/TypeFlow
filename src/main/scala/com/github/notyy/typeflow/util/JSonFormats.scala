package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.example1.UserInputInterpreter.{CreateModelCommand, UnknownCommand}
import org.json4s.{DefaultFormats, Formats, ShortTypeHints}

object JSonFormats {
  val userInterpreterResultFormats: Formats = DefaultFormats.withHints(ShortTypeHints(List(classOf[UnknownCommand], classOf[CreateModelCommand])))

}
