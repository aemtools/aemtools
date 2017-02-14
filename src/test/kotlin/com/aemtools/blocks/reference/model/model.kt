package com.aemtools.blocks.reference.model

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import junit.framework.Assert
import junit.framework.TestCase.assertNotNull
import org.intellij.lang.annotations.Language
import java.util.*

/**
 * @author Dmytro Troynikov
 */
interface IReferenceTestFixture {

    /**
     * Add html file to the fixture.
     * @param name name of the file
     * @param text the body of html file
     */
    fun addHtml(name: String,
                @Language("HTML") text: String)

    /**
     * Add java class to the fixture.
     * @param text java class
     */
    fun addClass(name: String,
                 @Language("Java") text: String)

    /**
     * Assert that the element under caret should be resolved to item of specified type.
     * @param type the type of resolution item
     * @param strict for _false_ [Class.isAssignableFrom] will be called, otherwise the classes will
     * be compared on equality
     */
    fun shouldResolveTo(type: Class<out PsiElement>, strict: Boolean = false)

    /**
     * Assert that resolved item should contained specified text.
     * @param text the text
     */
    fun shouldContainText(text: String)

    /**
     * Initialize the fixture.
     */
    fun init()

    /**
     * Perform the test.
     */
    fun test()

}

class ReferenceTestFixture(val fixture: JavaCodeInsightTestFixture)
    : IReferenceTestFixture {
    val files: ArrayList<IFileFixtureDescriptor> = ArrayList()

    var shouldBeResolvedTo: Class<out PsiElement>? = null
    var shouldBeResolvedStrictly: Boolean = false
    var shouldContainText: String? = null

    override fun shouldResolveTo(type: Class<out PsiElement>, strict: Boolean) {
        shouldBeResolvedTo = type
        shouldBeResolvedStrictly = strict
    }

    override fun shouldContainText(text: String) {
        shouldContainText = text
    }

    override fun test() {
        val referenceUnderCaret = fixture.file.findReferenceAt(fixture.editor.caretModel.offset)
                ?: throw AssertionError("Unable to find reference in: ${fixture.file.text}")

        val resolvedReference = referenceUnderCaret.resolve()

        val _shouldBeResolvedTo = shouldBeResolvedTo
        if (_shouldBeResolvedTo != null) {
            if (shouldBeResolvedStrictly) {
                Assert.assertEquals(_shouldBeResolvedTo, resolvedReference?.javaClass)
            } else {
                assertNotNull("Unable to resolve reference $referenceUnderCaret", resolvedReference)

                Assert.assertTrue(
                        "${_shouldBeResolvedTo.canonicalName} is not assignable from ${resolvedReference?.javaClass?.canonicalName}",
                        _shouldBeResolvedTo.isAssignableFrom(resolvedReference?.javaClass))
            }
        }

        if (shouldContainText != null) {
            Assert.assertEquals(shouldContainText, resolvedReference?.text)
        }
    }

    override fun init() = files.forEach { it.initialize() }

    override fun addHtml(name: String, text: String) {
        files.add(TextFileFixtureDescriptor(name, text, fixture))
    }

    override fun addClass(name: String, text: String) {
        files.add(JavaClassFileFixtureDescriptor(name, text, fixture))
    }

}

