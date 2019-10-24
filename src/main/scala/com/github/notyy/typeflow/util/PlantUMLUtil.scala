package com.github.notyy.typeflow.util

object PlantUMLUtil {
  type DefiBlock = Vector[String]
  type ConnBlock = Vector[String]

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
