package com.notyy.visualfp.example1

import com.notyy.visualfp.example1.UserInputIntepreter.AddElementCommand

object AddModelElement {
  def execute(savedModel: Model, addModelElementCommand: AddElementCommand): Model = {
    val newElement = Element(addModelElementCommand.elementType, addModelElementCommand.elementName)
    savedModel.copy(elements = savedModel.elements.appended(newElement))
  }
}
