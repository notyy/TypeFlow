package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.UserInputIntepreter.AddElementCommand

object AddModelElement {
  def execute(savedModel: Model, addModelElement: AddElementCommand): Model = {
    val newElement = Element(addModelElement.elementType, addModelElement.elementName)
    savedModel.copy(elements = savedModel.elements.appended(newElement))
  }
}
