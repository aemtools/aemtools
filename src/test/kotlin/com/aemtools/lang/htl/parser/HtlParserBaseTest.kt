package com.aemtools.lang.htl.parser

import com.aemtools.blocks.parser.BaseParserTest
import com.aemtools.lang.htl.HtlParserDefinition

/**
 * @author Dmytro Troynikov
 */
abstract class HtlParserBaseTest(dataPath: String, fileExt: String = "html")
  : BaseParserTest(dataPath, fileExt, HtlParserDefinition()) {

  override fun getTestDataPath(): String = "src/test/resources"

  override fun checkAllPsiRoots() = false

}
