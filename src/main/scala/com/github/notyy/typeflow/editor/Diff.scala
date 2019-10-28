package com.github.notyy.typeflow.editor

import com.github.notyy.typeflow.util.PlantUMLUtil
import com.typesafe.scalalogging.Logger

object Diff {
  private val logger = Logger(Diff.getClass)

  private val lsep: String = System.lineSeparator()

  //TODO should be a Try
  def execute(thisPlantUML: String, targetPlantUML: String): String = {
    val (thisDefBlock, thisConnBlock) = PlantUMLUtil.separatorBlocks(thisPlantUML)
    val (targetDefBlock, targetConnBlock) = PlantUMLUtil.separatorBlocks(targetPlantUML)
    val bothDefBlock = thisDefBlock.intersect(targetDefBlock)
    val thisMoreDefBlock = thisDefBlock.diff(targetDefBlock).map(defi => s"$defi #LightGreen")
    val thisLessDefBlock = targetDefBlock.diff(thisDefBlock).map(defi => s"$defi #LightCoral")

    val bothConnBlock = thisConnBlock.intersect(targetConnBlock)
    val thisMoreConnBlock = thisConnBlock.diff(targetConnBlock).map(conn => s"$conn #LightGreen")
    val thisLessConnBlock = targetConnBlock.diff(thisConnBlock).map(conn => s"$conn #LightCoral")

    val rs =
      s"""
         |@startuml
         |${(bothDefBlock ++ thisMoreDefBlock ++ thisLessDefBlock).mkString(lsep)}
         |
         |${(bothConnBlock ++ thisMoreConnBlock ++ thisLessConnBlock).mkString(lsep)}
         |@enduml
         |""".stripMargin
    rs
  }

}
