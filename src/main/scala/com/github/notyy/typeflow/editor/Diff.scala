package com.github.notyy.typeflow.editor

import com.typesafe.scalalogging.Logger

object Diff {
  private val logger = Logger(Diff.getClass)

  type DefiBlock = Vector[String]
  type ConnBlock = Vector[String]

  private val lsep: String = System.lineSeparator()

  def execute(thisPlantUML: String, targetPlantUML: String): String = {
    val (thisDefBlock, thisConnBlock) = separatorBlocks(thisPlantUML)
    val (targetDefBlock, targetConnBlock) = separatorBlocks(targetPlantUML)
    val bothDefBlock = thisDefBlock.intersect(targetDefBlock)
    val thisMoreDefBlock = thisDefBlock.diff(targetDefBlock).map(defi => s"$defi #LightGreen")
    val thisLessDefBlock = targetDefBlock.diff(thisDefBlock).map(defi => s"$defi #LightCoral")

    val bothConnBlock = thisConnBlock.intersect(targetConnBlock)
    val thisMoreConnBlock = thisConnBlock.diff(targetConnBlock).map(conn => s"$conn #LightGreen")
    val thisLessConnBlock = targetConnBlock.diff(thisConnBlock).map(conn => s"$conn #LightCoral")

    val rs = s"""
                |@startuml
                |${(bothDefBlock ++ thisMoreDefBlock ++ thisLessDefBlock).mkString(lsep)}
                |
                |${(bothConnBlock ++ thisMoreConnBlock ++ thisLessConnBlock).mkString(lsep)}
                |@enduml
                |""".stripMargin
    rs
  }

  def separatorBlocks(plantUML: String):(DefiBlock, ConnBlock) = {
//    logger.debug(s"separate definition block and connection block from$lsep$plantUML")
    val lines = plantUML.linesIterator.toVector
    val defiBlock = lines.filter(_.startsWith("class"))
    val connBlock = lines.filter(_.contains("-->"))
//    logger.debug(s"defiBlock:${lsep}${defiBlock.mkString(lsep)}")
//    logger.debug(s"connBlock:${lsep}${connBlock.mkString(lsep)}")
    (defiBlock, connBlock)
  }
}
