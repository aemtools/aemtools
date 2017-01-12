package com.aemtools.lang.htl.parser

import com.aemtools.lang.htl.HtlParserDefinition
import com.intellij.lang.ParserDefinition
import com.intellij.testFramework.ParsingTestCase

/**
 * @author Dmytro Troynikov
 */
abstract class HtlParserBaseTest(dataPath: String, fileExt: String = "html", lowercaseFirstLetter: Boolean = true)
: ParsingTestCase(dataPath, fileExt, lowercaseFirstLetter, HtlParserDefinition()) {

    fun doTest() = doTest(true)

    override fun getTestDataPath(): String = "src/test/resources"

    override fun checkAllPsiRoots() = false

}