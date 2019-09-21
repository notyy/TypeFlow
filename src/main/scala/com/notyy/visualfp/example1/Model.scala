package com.notyy.visualfp.example1

case class Model(name: String, elements: Vector[Element], connections: Vector[Connection])
case class Element(elementType: String, name: String)
case class Connection(from: String, to: String)
