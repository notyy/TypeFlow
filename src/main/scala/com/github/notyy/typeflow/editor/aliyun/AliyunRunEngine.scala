package com.github.notyy.typeflow.editor.aliyun

import com.aliyun.oss.OSS
import com.aliyuncs.fc.client.FunctionComputeClient
import com.aliyuncs.fc.request.InvokeFunctionRequest
import com.github.notyy.typeflow.domain._
import com.github.notyy.typeflow.editor.{InterpreterResult, Path, PlantUML, PlantUML2Model, ReadFile, UnknownCommand}
import com.github.notyy.typeflow.util.{JSONUtil, JSonFormats, ModelUtil, Param, ReflectRunner, TypeUtil}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}


case class AliyunRunEngine(model: Model,
                          fcClient: FunctionComputeClient) {
  private val logger = Logger(classOf[AliyunRunEngine])

  type InstanceId = String
  type InputIndex = Int
  //TODO for function that have multiple inputs, when we got one input ready,
  //we have to put it to cache, waiting for other inputs to be ready.
  //this maybe dangerous, because it occupy uncontrolled num of memory.
  private val waitingParams: scala.collection.mutable.Map[InstanceId, Map[InputIndex, Object]] = scala.collection.mutable.Map.empty

  def startFlow(output: Param[Object], outputFrom: Instance): Unit = {
    val nextInsts: Vector[(Instance, InputIndex)] = nextInstances(output, outputFrom)
    callNextInstances(output, nextInsts)
  }

  //TODO consider shapeless later
  def vectorToParamTuple(vec: Vector[Object]): Param[Object] = vec match {
    case Vector(a,b) => Param(a,b)
    case Vector(a,b,c) => Param(a,b,c)
    case Vector(a,b,c,d) => Param(a,b,c,d)
    case Vector(a,b,c,d,e) => Param(a,b,c,d,e)
    case Vector(a,b,c,d,e,f) => Param(a,b,c,d,e,f)
  }

  //@tailrec    TODO failed tailrec, must solve this later
  //TODO copied from LocalRunEngine, duplicated code need to be cleaned
  private def callNextInstances(output: Param[Object], instances: Vector[(Instance, InputIndex)]): Unit = {
    instances.foreach { case (ins, idx) =>
      if (ins.definition.inputs.size > 1) {
        if (waitingParams.contains(ins.id)) {
          val prevParams = waitingParams(ins.id)
          val currParams = prevParams + (idx -> output.value)
          if (currParams.size == ins.definition.inputs.size) {
            //enough parameters
            callInstance(vectorToParamTuple(currParams.toVector.sortBy(_._1).map(_._2)), ins).map {
              nextOutput =>
                waitingParams.remove(ins.id)
                val nextIns = nextInstances(nextOutput, ins)
                callNextInstances(nextOutput, nextIns)
            } match {
              case Success(value) =>
              case Failure(exception) => {
                println(s"calling ${ins.id} with param $output failed ${exception.getMessage}")
              }
            }
          } else {
            waitingParams += (ins.id -> currParams)
          }
        } else {
          waitingParams += (ins.id -> Map(idx -> output.value))
        }
      } else {
        callInstance(output, ins).map {
          nextOutput =>
            val nextIns = nextInstances(nextOutput, ins)
            callNextInstances(nextOutput, nextIns)
        }
      }
    }
  }

  //TODO maybe can use type lambda?
  private def callInstance(outputs: Param[Object], instance: Instance): Try[Param[Object]] = {
    logger.debug(s"calling ${instance.id} with parameter '$outputs'")
    val invkReq = new InvokeFunctionRequest(model.name, instance.definition.name)
    val payload = JSONUtil.toJSON(outputs)
    invkReq.setHeader("Content-Type", "application/json");
    invkReq.setPayload(payload.getBytes)
    val invokeResp = new String(fcClient.invokeFunction(invkReq).getPayload)
    logger.debug(s"result of calling ${instance.id} with parameter '$outputs' is '$invokeResp'")
    JSONUtil.fromJSON[Param[Object]](invokeResp)
  }

  def nextInstances(output: Param[Object], outputFrom: Instance): Vector[(Instance, InputIndex)] = {
    logger.debug(s"looking for nextInstance for ${outputFrom.id}.$output")
    val flow = model.activeFlow.get
    val nIns = outputFrom match {
      case Instance(id, AliyunHttpInputEndpoint(name, o)) => {
        logger.debug(s"outputs from InputEndpoint $id is ${output.value}, now looking for next instances")
        val conns: Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
      case Instance(id, PureFunction(name, inputType, outputs)) => {
        logger.debug(s"outputs from PureFunction $id is ${output.value}, now looking for next instances")
        val outputType: OutputType = OutputType(TypeUtil.getTypeShortName(output.value))
        logger.debug(s"outputType name of parameter is ${outputType.name}")
        logger.debug(s"PureFunction $name's designed outputs are ${outputs.mkString(",")}")
        //TODO solve this .get later
        val index = 1
//          outputs.find { ot =>
//          val designedOutputTypeName = ModelUtil.removePrefix(ot.outputType.name).split('.').last
//          logger.debug(s"comparing designed outputTypeName $designedOutputTypeName to actually outputTypeName ${outputType.name}")
//          designedOutputTypeName == outputType.name
//        }.get.index
        //        logger.debug(s"index is $index")
        val conns = flow.connections.filter(conn => conn.fromInstanceId == outputFrom.id && conn.outputIndex == index)
        //        logger.debug(s"find ${conns.size} connections")
        connections2instances(conns)
      }
      case Instance(id, OutputEndpoint(name, inputs, outputType, errorOutputs)) => {
        logger.debug(s"outputs from OutputEndpoint $id is $output, now looking for next instances")
        val conns: Vector[Connection] = flow.connections.filter(_.fromInstanceId == outputFrom.id)
        connections2instances(conns)
      }
    }
    logger.debug(s"next should call:[${nIns.map { case (ins, idx) => s"${ins.id}.$idx" }.mkString(",")}]")
    nIns
  }

  private def connections2instances(conns: Vector[Connection]): Vector[(Instance, InputIndex)] = {
    logger.debug(s"connections2Instances $conns")
    conns.map(conn => (model.activeFlow.get.instances.find(_.id == conn.toInstanceId).get, conn.inputIndex))
  }
}

object AliyunRunEngine {
  def runFlow(bucketName: String, objectName: String, inputEndpointName: String, output: Param[Object],fcClient: FunctionComputeClient, ossClient: OSS): Try[Unit] = {
    ReadFileFromAliyunOSS(ossClient).execute(bucketName, s"$objectName.puml").map { puml =>
      val model = PlantUML2Model.execute(PlantUML(objectName, puml))
      val inputEndpoint = model.activeFlow.get.instances.find(_.id == inputEndpointName).get
      val aliyunRunEngine = AliyunRunEngine(model,fcClient)
      aliyunRunEngine.startFlow(output, inputEndpoint)
    }
  }
}
