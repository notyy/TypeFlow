package com.github.notyy.typeflow.editor


import com.github.notyy.typeflow.domain.{Definition, Element, Flow, Input, InputEndpoint, InputType, Model, Output, OutputEndpoint, OutputType, PureFunction}
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

  def createInputEndpoint(ele: Element, descriptions: Vector[UMLDescription]): InputEndpoint = {
    descriptions.foreach(desc => {
      if(ele.name == desc.fromName) {
        val definition = InputEndpoint(ele.name, OutputType(desc.toName))
        return definition
      }
    })
    null
  }

  def createPureFunction(ele: Element, descriptions: Vector[UMLDescription]): PureFunction = {
    val outputs: ArrayBuffer[Output] = ArrayBuffer()
    val inputs: ArrayBuffer[Input] = ArrayBuffer()
    descriptions.foreach(desc => {
      if(desc.fromName == ele.name) {
        outputs.addOne(Output(OutputType(desc.toName), outputs.length + 1))
      }
      if(desc.toName == ele.name) {
        inputs.addOne(Input(InputType(desc.fromName), inputs.length + 1))
      }
    })
    PureFunction(ele.name, inputs.toVector, outputs.toVector)
  }

  def createOutputEndpoint(ele: Element, descriptions: Vector[UMLDescription]): OutputEndpoint = {
    var outputType: OutputType = null
    val inputs: ArrayBuffer[Input] = ArrayBuffer()
    val errorOutputs: Vector[Output] = null
    descriptions.foreach(desc => {
      if(desc.fromName == ele.name) {
        outputType = OutputType(desc.toName)
      }
      if(desc.toName == ele.name) {
        inputs.addOne(Input(InputType(desc.fromName), inputs.length + 1))
      }
    })
    OutputEndpoint(ele.name, inputs.toVector, outputType, errorOutputs)
  }

  def createDefinition(ele: Element, descriptions: Vector[UMLDescription]): Definition = {
    ele.elementType match {
      case "InputEndpoint" => createInputEndpoint(ele, descriptions)
      case "PureFunction" =>createPureFunction(ele, descriptions)
      case "OutputEndpoint" => createOutputEndpoint(ele, descriptions)
    }
  }

  def createDefinitions(elements: Vector[Element], descriptions: Vector[UMLDescription]): Vector[Definition] = {
    elements.map(ele => createDefinition(ele, descriptions))
  }

  def execute(plantUML: PlantUML): Model = {
    val umlStr = plantUML.value
    println(s"$umlStr")
    println("----------")

    val name: String = "typeflow_editor"
    val umlStrings: Array[String] = umlStr.split('\n')
    val descriptions: ArrayBuffer[UMLDescription] = ArrayBuffer()
    val elements: ArrayBuffer[Element] = ArrayBuffer()

    //deal with the plantuml info
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

    //get definitions
    val definitions: Vector[Definition] = createDefinitions(elements.toVector, descriptions.toVector)






    null
    //Model(name, definitions, flows, activeFlow)
  }
}
