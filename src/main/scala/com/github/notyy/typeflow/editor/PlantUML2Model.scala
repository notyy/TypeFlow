package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.domain._
import com.github.notyy.typeflow.util.PlantUMLUtil
import com.typesafe.scalalogging.Logger

import scala.util.matching.Regex

//TODO use type-flow to rewrite this tool. eat my own dog food!
object PlantUML2Model {
  private val logger = Logger(PlantUML2Model.getClass)
  type ElementName = String
  type ElementType = String
  type InputTypeName = String
  type OutputTypeName = String

  private val ElementPattern: Regex = """class (.*) <<(.*)>>""".r
  private val DescriptionPattern: Regex = """(.*) --> (.*)""".r

  def execute(plantUML: PlantUML): Model = {
    val modelName = plantUML.modelName
    val content = plantUML.content
    val (rawDefiBlock, connBlock) = PlantUMLUtil.separatorBlocks(content)
    val rawDefiNameType: Map[ElementName, ElementType] = rawDefiBlock.map {
      case ElementPattern(elementName, elementType) => (elementName, elementType)
    }.toMap[ElementName, ElementType]
    logger.debug(s"find ${rawDefiNameType.keySet.size} raw definitions")

    val fromTos: Vector[(ElementName, String)] =
      connBlock.map {
        case DescriptionPattern(from, to) => (from, to)
      }

    val instanceToOutput: Map[ElementName, Vector[Output]] = (fromTos.
      filter { case (from, to) => rawDefiNameType.keySet.contains(from) }.
      groupMap(_._1)(_._2)).mapValues(_.foldLeft(Vector.empty[Output])((acc, ot) => acc.appended(Output(OutputType(ot), acc.size + 1)))).toMap
    val outputToInstance = fromTos.filter { case (from, to) => rawDefiNameType.keySet.contains(to) }
    val instanceFromInput: Map[ElementName, Vector[Input]] = (outputToInstance.map { case (from, to) => (to, from) }.
      groupMap(_._1)(_._2)).mapValues(_.foldLeft(Vector.empty[Input])((acc, it) => acc.appended(Input(InputType(it), acc.size + 1)))).toMap

    val definitionsWithDecorates: Vector[Definition] = rawDefiNameType.toVector.map {
      case (name, "CommandLineInputEndpoint") => CommandLineInputEndpoint(name, instanceToOutput(name).map(ot => OutputType(ot.outputType.name)).head)
      case (name, "AliyunHttpInputEndpoint") => AliyunHttpInputEndpoint(name, instanceToOutput(name).map(ot => OutputType(ot.outputType.name)).head)
      case (name, "PureFunction") => {
        PureFunction(name,
          instanceFromInput(name),
          instanceToOutput(name))
      }
      case (name, "OutputEndpoint") => {
        OutputEndpoint(name,
          instanceFromInput(name),
          instanceToOutput.get(name).map(_.head.outputType).getOrElse(OutputType("Unit")), Vector.empty
        )
      }
    }

    val instances: Vector[Instance] =
      rawDefiNameType.toVector.map { case (elementName, elementType) =>
        val defiName = elementName.split("::").last
        val definition = definitionsWithDecorates.find(_.name == defiName).get
        Instance(elementName, definition)
      }
    logger.debug(s"extract ${instances.size} instances")

    val connections: Vector[Connection] =
      instanceToOutput.toVector.flatMap {
        case (instanceId, outputs) =>
          outputs.flatMap{ ot =>
            val outputIndex: Int = definitionsWithDecorates.find(_.name == instanceId).get match {
              case CommandLineInputEndpoint(name, outputType) => 1
              case AliyunHttpInputEndpoint(name, outputType) => 1
              case PureFunction(name, inputs, outputs) => outputs.find(_ == ot).get.index
              case OutputEndpoint(name, inputs, outputs, errorOutputs) => 1
            }
            val conntions: Vector[Connection] = {
              val instanceConnectedByInput: Map[ElementName, Vector[Input]] = instanceFromInput.filter {
                case (elementName, inputs) => inputs.exists(_.inputType.name == ot.outputType.name)
              }

              instanceConnectedByInput.map {
                case (eleName, ins) =>  {
                  val inputIndex = ins.find(_.inputType.name == ot.outputType.name).get.index
                  Connection(instanceId, outputIndex, eleName, inputIndex)
                }
              }.toVector
            }
            conntions
          }
      }
    logger.debug(s"extract connections:${System.lineSeparator()}${connections.mkString(System.lineSeparator())}")

    val cleanDefinitions: Vector[Definition] = definitionsWithDecorates.filterNot(_.name.contains("::")).
      map{ defi =>
        val cleanInputs = defi.inputs.map(in => Input(InputType(in.inputType.name.split("::").last), in.index))
        defi match {
          case i: InputEndpoint => i
          case p: PureFunction => p.copy(inputs = cleanInputs)
          case o: OutputEndpoint => o.copy(inputs = cleanInputs)
        }
      }

    val flow = Flow(modelName, instances, connections)
    val flows: Vector[Flow] = Vector(flow)

    //create active flow
    val activeFlow: Option[Flow] = Some(flow)

    //create model
    val model = Model(modelName,cleanDefinitions, flows, activeFlow)
    model
  }
}