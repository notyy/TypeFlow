package com.github.notyy.typeflow.editor.aliyun

case class AliyunFunction(name: String, handler: String, trigger: Option[Trigger])

case class Trigger(name: String, triggerType: String)