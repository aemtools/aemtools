package com.aemtools.blocks.rename.model.fixture

/**
 * @author Dmytro Troynikov
 */
class RenameState : IRenameState {

  val files: ArrayList<SimpleFileDescriptor> = ArrayList()

  override fun addHtml(name: String, text: String) = addFile(name, text)

  override fun addClass(name: String, text: String) = addFile(name, text)

  override fun addXml(name: String, text: String) = addFile(name, text)

  override fun addFile(name: String, text: String) {
    files.add(SimpleFileDescriptor(name, text))
  }

}

data class SimpleFileDescriptor(val name: String, val text: String)
