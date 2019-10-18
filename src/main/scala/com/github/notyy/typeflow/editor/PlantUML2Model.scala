package com.github.notyy.typeflow.editor


import com.github.notyy.typeflow.domain.{Definition, Element, Flow, Function, InputEndpoint, Model, OutputEndpoint, OutputType}
import org.hamcrest.Description

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

object PlantUML2Model {

  private val ElementPattern: Regex = """class (.*) <<(.*)>>""".r
  private val  DescriptionPattern: Regex = """(.*) --> (.*)""".r

//  def createDefinition(definitionName: String, className: String): Definition = {
//    var definition: Definition = className match {
//      case "InputEndpoint" => InputEndpoint(definitionName, null)
//      case "Function" => Function(definitionName, null, null)
//      case "OutputEndpoint" => OutputEndpoint(definitionName, null, null, null)
//    }
//    if(definition.isInstanceOf[InputEndpoint]) {
//      var outputType = definition.asInstanceOf[InputEndpoint].outputType
//    }
//    definition
//    //println(s"=====Defintion=====$definitionName   +   $className")
  //  }

  def createElement(elementType: String, elementName: String): Element = {
    Element(elementType, elementName)
  }

  def createDescription(fromName: String, toName: String): UMLDescription = {
    UMLDescription(fromName, toName)
  }


  def otherSituation(): Unit = {
    println("other situation")
  }


  def execute(plantUML: PlantUML): Model = {
    val umlStr = plantUML.value
    println(s"$umlStr")
    println("----------")

    val name: String = "typeflow_editor"
    val umlStrings: Array[String] = umlStr.split('\n')
    val descriptions: ArrayBuffer[UMLDescription] = ArrayBuffer()
    val elements: ArrayBuffer[Element] = ArrayBuffer()

    umlStrings.foreach((uml:String) => {
      val plantUMLMatcher = uml match {
        case ElementPattern(elementName, elementType) => createElement(elementType, elementName)
        case DescriptionPattern(fromName, toName) => createDescription(fromName, toName)
        case _ => otherSituation()
      }
      if(plantUMLMatcher.isInstanceOf[Element]) {
        elements.addOne(plantUMLMatcher.asInstanceOf[Element])
      }
      if(plantUMLMatcher.isInstanceOf[UMLDescription]) {
        descriptions.addOne(plantUMLMatcher.asInstanceOf[UMLDescription])
      }
    })




    null
    //Model(name, definitions, flows, activeFlow)
  }
}
