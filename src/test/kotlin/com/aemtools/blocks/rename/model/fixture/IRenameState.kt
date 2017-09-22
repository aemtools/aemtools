package com.aemtools.blocks.rename.model.fixture

import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Troynikov
 */
interface IRenameState {

  /**
   * Add html file to state.
   *
   * @param name name of file
   * @param text the content of html file
   */
  fun addHtml(name: String, @Language("HTML") text: String)

  /**
   * Add java class to state.
   *
   * @param name name of file
   * @param text the content of class file
   */
  fun addClass(name: String, @Language("Java") text: String)

  /**
   * Add XML file to state.
   *
   * @param name name of the file
   * @param text the content of xml file
   */
  fun addXml(name: String, @Language("XML") text: String)

  /**
   * Add arbitrary file to the state.
   *
   * @param name the name of the file
   * @param text the content of the file
   */
  fun addFile(name: String, text: String)

}
