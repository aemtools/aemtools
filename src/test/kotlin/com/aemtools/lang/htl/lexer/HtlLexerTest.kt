package com.aemtools.lang.htl.lexer

import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.lexer.Lexer
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.testFramework.LexerTestCase
import com.intellij.testFramework.UsefulTestCase
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

/**
* @author Dmytro Troynikov.
*/
class HtlLexerTest : LexerTestCase(), HtlTestCase {
    override fun getTestDataPath(): String
        = "com/aemtools/lang/htl/lexer/fixtures"

    fun testStringLiteral() = doTest()
    fun testDoubleQuotedStringLiteral() = doTest()
    fun testStringWithEscapeSymbols() = doTest()
    fun testEmptyString() = doTest()

    fun testNumberLiteral() = doTest()
    fun testBooleanLiteral() = doTest()
    fun testLogicalAnd() = doTest()
    fun testLogicalOr() = doTest()
    fun testComparison() = doTest()
    fun testEquality() = doTest()
    fun testGroupedBooleanExpression() = doTest()

    fun testSimpleOption() = doTest()
    fun testMultipleOptions() = doTest()
    fun testOptionStandalone() = doTest()

    fun testTernaryOperator() = doTest()

    fun testVariableAccess() = doTest()
    fun testVariableAccessWithContext() = doTest()
    fun testNegate() = doTest()

    fun testElInAttribute() = doTest()
    fun testElInScript() = doTest()
    fun testElInStyle() = doTest()
    fun testElWithContext() = doTest()

    fun testComplexFile() = doTest()

    fun testUnclosedEl() = doTest()
    fun testEmptyEl() = doTest()

    override fun createLexer(): Lexer = com.aemtools.lang.htl.lexer.HtlLexer()

    override fun getDirPath(): String? = "com.aemtools.lang.htl.lexer"

    protected fun doTest(lexer: Lexer = createLexer()) {
        val filePath = pathToSourceTestFile(getTestName(true))

        var text = ""
        try {
            val fileText = FileUtil.loadFile(filePath.toFile(), Charsets.UTF_8)
            text = StringUtil.convertLineSeparators(if (shouldTrim()) fileText.trim() else fileText)
        } catch (e: IOException) {
            fail("Unable to load file $filePath: ${e.message}")
        }
        doTest(text, null, lexer)
    }

    override fun doTest(text: String, expected: String?, lexer: Lexer) {
        val result = printTokens(text, 0, lexer)
        if (expected != null) {
            UsefulTestCase.assertSameLines(expected, result)
        } else {
            UsefulTestCase.assertSameLinesWithFile(pathToGoldTestFile(getTestName(true)).toFile().canonicalPath, result)
        }
    }

}

interface HtlTestCase {
    fun getTestDataPath(): String

    companion object {
        val testResourcesPath = "src/test/resources"
    }
}

fun HtlTestCase.pathToSourceTestFile(name: String): Path =
        Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.${HtlFileType.defaultExtension}")

fun HtlTestCase.pathToGoldTestFile(name: String): Path =
        Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.txt")