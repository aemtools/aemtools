package com.aemtools.test.parser

import com.intellij.lang.ParserDefinition
import com.intellij.testFramework.ParsingTestCase

/**
 * @author Dmytro Primshyts
 */
abstract class BaseParserTest(dataPath: String,
                              fileExt: String,
                              parserDefinition: ParserDefinition,
                              lowercaseFirstLetter: Boolean = true)
  : ParsingTestCase(dataPath, fileExt, lowercaseFirstLetter, parserDefinition) {

  fun doTest() = doTest(true)

  override fun getTestDataPath(): String = "src/test/resources"

  override fun checkAllPsiRoots() = false

}
