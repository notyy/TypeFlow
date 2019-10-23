package com.github.notyy.typeflow.tooling

import com.github.notyy.typeflow.editor.{Diff, Path, ReadFile, SaveToFile}

import scala.sys.process.Process

object DiffScript extends App {
  if (args.length == 4) {
    val plantUMLJarPath = args(0)
    val outputPath: Path = Path(args(3))
    (for {
      srcPlantUML <- ReadFile.execute(Path(args(1)))
      targetPlantUML <- ReadFile.execute(Path(args(2)))
    } yield Diff.execute(srcPlantUML, targetPlantUML)).
      foreach(diffPlantUML => SaveToFile.execute(outputPath,diffPlantUML))
    Process(s"java -jar $plantUMLJarPath  ${outputPath.value}").!!
    Process(s"open ${outputPath.value.dropRight("puml".length) ++ "png"}").!!
  } else {
    println("usage: java -jar type-flow{replace with your version no}.jar srcPlantUMLPath targetPlantUMLPath outputPath")
  }
}
