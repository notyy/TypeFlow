package com.github.notyy.typeflow.tooling

import com.github.notyy.typeflow.editor.{Content, Diff, ModelFilePath, OutputPath, Path, ReadFile, SaveToFile}

import scala.sys.process.Process

object DiffScript extends App {
  if (args.length == 4) {
    val plantUMLJarPath = args(0)
    val outputPath: OutputPath = OutputPath(args(3))
    (for {
      srcPlantUML <- ReadFile.execute(ModelFilePath(args(1)))
      targetPlantUML <- ReadFile.execute(ModelFilePath(args(2)))
    } yield Diff.execute(srcPlantUML, targetPlantUML)).
      foreach(diffPlantUML => new SaveToFile().execute(outputPath,Content(diffPlantUML)))
    Process(s"java -jar $plantUMLJarPath  ${outputPath.value}").!!
    Process(s"open ${outputPath.value.dropRight("puml".length) ++ "png"}").!!
  } else {
    println("usage: java -jar type-flow{replace with your version no}.jar srcPlantUMLPath targetPlantUMLPath outputPath")
  }
}
