package com.notyy.typeflow.domain

trait InputPattern

case object OneOfInput extends InputPattern
case object AllInput extends InputPattern
case object NoInput extends InputPattern
