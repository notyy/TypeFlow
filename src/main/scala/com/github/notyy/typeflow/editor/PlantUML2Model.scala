package com.github.notyy.typeflow.editor


import com.github.notyy.typeflow.domain.{Connection, Definition, Element, Flow, Input, InputEndpoint, InputType, Instance, Model, Output, OutputEndpoint, OutputType, PureFunction}
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.Elements
import org.hamcrest.Description

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

object PlantUML2Model {

  private val ElementPattern: Regex = """class (.*) <<(.*)>>""".r
  private val  DescriptionPattern: Regex = """(.*) --> (.*)""".r
  private val InputEndpointClass: String = "com.github.notyy.typeflow.domain.InputEndpoint"
  private val PureFunctionClass: String = "com.github.notyy.typeflow.domain.PureFunction"
  private val OutputEndpointClass: String = "com.github.notyy.typeflow.domain.OutputEndpoint"
  private val instancesFirst: mutable.Map[String, String] = mutable.Map()

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

  def isFunctionDuplicate(targetEle: Element, elements: Vector[Element]): Boolean = {
    if(targetEle.name.contains("::")) {
      return true
    }
    false
  }

  def getPureFunction(ele: Element, descriptions: Vector[UMLDescription]): PureFunction = {
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

  def addToMap(targetEle: Element, elements: Vector[Element]): Unit = {
    elements.foreach(ele => {
      if(targetEle.name.contains("::"+ele.name)){
        instancesFirst.addOne(targetEle.name, ele.name)
      }
    })
  }

  def createPureFunction(ele: Element, elements: Vector[Element], descriptions: Vector[UMLDescription]): PureFunction = {
    if(isFunctionDuplicate(ele, elements)) {
      addToMap(ele: Element, elements: Vector[Element])
      return null
    }
    getPureFunction(ele, descriptions)
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
    println(s")))))))()()()()()()  $outputType")
    OutputEndpoint(ele.name, inputs.toVector, outputType, errorOutputs)
  }

  def createDefinition(ele: Element, elements: Vector[Element], descriptions: Vector[UMLDescription]): Definition = {
    ele.elementType match {
      case "InputEndpoint" => createInputEndpoint(ele, descriptions)
      case "PureFunction" =>createPureFunction(ele, elements, descriptions)
      case "OutputEndpoint" => createOutputEndpoint(ele, descriptions)
    }
  }

  def createDefinitions(elements: Vector[Element], descriptions: Vector[UMLDescription]): Vector[Definition] = {
    elements.map(ele => createDefinition(ele, elements, descriptions)).filter(_ != null)
  }

  def getDefinition(name: String, definitions: Vector[Definition]): Definition = {
    definitions.foreach(defi => {
      if(defi.name == name) {
        return defi
      }
    })
    null
  }

  def createInstances(definitions: Vector[Definition], elements: Vector[Element]): Vector[Instance] = {
    val instances: ArrayBuffer[Instance] = ArrayBuffer()
    instances.addAll(definitions.map(defi => Instance(defi)))
    for((k, v) <- instancesFirst) {
      instances.addOne(Instance(k, getDefinition(v, definitions)))
    }
    instances.toVector
  }

  def getToInstanceId(outputType: OutputType, descriptions: Vector[UMLDescription]) : String = {
    if(outputType == null) {
      return null
    }
    descriptions.foreach(desc => {
      if(desc.fromName == outputType.name) {
        //consider whether change to Array, as one type of outputType may point to the
        return desc.toName
      }
    })
    null
  }

  def createInputConnection(ins: Instance, descriptions: Vector[UMLDescription]): Vector[Connection] = {
    val fromInstanceId = ins.id
    val inputEndpoint = ins.definition.asInstanceOf[InputEndpoint]
    val outputType = inputEndpoint.outputType
    val toInstanceId = getToInstanceId(outputType, descriptions)
    //test
    Vector(Connection(fromInstanceId, 1, toInstanceId, inputIndex = 0))
  }

  def createFunctionConnection(ins: Instance, descriptions: Vector[UMLDescription]): Vector[Connection] = {
    val connections: ArrayBuffer[Connection] = ArrayBuffer()
    val fromInstanceId = ins.id
    val inputEndpoint = ins.definition.asInstanceOf[PureFunction]
    val outputs = inputEndpoint.outputs
    outputs.foreach(out => {
      val index = out.index
      val toInstanceId = getToInstanceId(out.outputType, descriptions)
      //test
      connections.addOne(Connection(fromInstanceId, index, toInstanceId, inputIndex = 0))
    })
    connections.toVector
  }

  def createOutputConnection(ins: Instance, descriptions: Vector[UMLDescription]): Vector[Connection] = {
    //now only need to focus on the one OutputTpye but not the errorOutputs
    val fromInstanceId = ins.id
    val outputEndpoint = ins.definition.asInstanceOf[OutputEndpoint]
    val outputType = outputEndpoint.outputType
    val toInstanceId = getToInstanceId(outputType, descriptions)
    if(toInstanceId != null) {
      //test
      Vector(Connection(fromInstanceId, 1, toInstanceId, inputIndex = 0))
    }
    null
  }

  def createConnections(instances: Vector[Instance], descriptions: Vector[UMLDescription]): Vector[Connection] = {
    val connections: ArrayBuffer[Connection] = ArrayBuffer()
    instances.foreach(ins => {
      val test = ins.definition.getClass.getName
      println(s"+++++++++++++  $test")
      val connectionMatchers = ins.definition.getClass.getName match {
        case InputEndpointClass => createInputConnection(ins, descriptions)
        case PureFunctionClass => createFunctionConnection(ins, descriptions)
        case OutputEndpointClass => createOutputConnection(ins, descriptions)
        case _ => Nil
      }
      if(connectionMatchers != null && connectionMatchers != null) {
        connectionMatchers.foreach(conn => {
        connections.addOne(conn)
        })
      }
    })
    connections.toVector
  }

  def createFlows(name: String, instances: Vector[Instance], connections: Vector[Connection]): Vector[Flow] = {
    Vector(Flow(name, instances, connections))
  }


  def createActiveFlow(flows: Vector[Flow]): Option[Flow] = {
    Option(flows.apply(0))
  }

  def execute(plantUML: PlantUML): Model = {
    val umlStr = plantUML.content
    println(s"$umlStr")
    println("----------")

    val name: String = plantUML.modelName
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

    //create definitions
    val definitions = createDefinitions(elements.toVector, descriptions.toVector)

    //create instances
    val instances = createInstances(definitions, elements.toVector)

    //create connections
    val connections = createConnections(instances, descriptions.toVector)

    //create flows
    val flowsName = "minimalFlow"
    val flows: Vector[Flow] = createFlows(flowsName, instances, connections)

    //create active flow
    val activeFlow: Option[Flow] = createActiveFlow(flows)

    //create model
    val model = Model(name, definitions, flows, activeFlow)
    model
  }
}


