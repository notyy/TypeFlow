package com.notyy.visualfp.domain

import com.notyy.visualfp.domain.Common.IDType

case class Model(metaInfo: ModelMetaInfo, Elements: Vector[Element])
case class ModelMetaInfo(author: String)
case class Author(userId: IDType, name: String)
