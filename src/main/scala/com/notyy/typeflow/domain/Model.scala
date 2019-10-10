package com.notyy.typeflow.domain

import com.notyy.typeflow.domain.Common.IDType

case class Model(metaInfo: ModelMetaInfo, Elements: Vector[Element])
case class ModelMetaInfo(author: String)
case class Author(userId: IDType, name: String)
