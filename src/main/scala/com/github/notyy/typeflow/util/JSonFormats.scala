package com.github.notyy.typeflow.util

import com.github.notyy.typeflow.domain.{CommandLineInputEndpoint, OutputEndpoint, PureFunction}
import com.github.notyy.typeflow.editor.{CreateModelCommand, UnknownCommand}
import org.json4s.{DefaultFormats, Formats, ShortTypeHints}

object JSonFormats {
  val userInterpreterResultFormats: Formats = DefaultFormats.withHints(ShortTypeHints(List(classOf[UnknownCommand], classOf[CreateModelCommand])))
  val modelFormats: Formats = DefaultFormats.withHints(ShortTypeHints(List(classOf[CommandLineInputEndpoint], classOf[PureFunction], classOf[OutputEndpoint])))
}
