# 类型流（TypeFlow）

**以下文档都基于您当前处于本工程源代码路径内来进行操作**
### 1. 可视化的查看plantuml文件的差异
`script/diff.sh 源puml文件路径 比较对象puml文件路径 比较结果输出路径（需要完整文件名）`

例如

`script/diff.sh fixtures/diff/newModel.puml fixtures/diff/multi_param.puml localoutput/diff.puml`

源文件多于比较对象的元素用浅绿色显示，表示新增;少于比较对象的元素用浅红色显示,表示删除。

注意这个命令默认plantuml.jar位于`./externalJars/plantuml.jar` 

考虑到plantuml.jar的license，本人不是法律专家，搞不清楚状况，故不敢把plantuml.jar打包进工程。 请用户自行到http://plantuml.com/zh/download下载