package com.aemtools.lang.htl.refactoring

import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.TestCase

/**
 * @author Dmytro_Troynikov
 */
class ReferenceTest : LightCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String =
            "src/test/resources/refactoring/"

    //TODO add tests

    fun testReferenceToField() {
        myFixture.configureByText("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                ${'$'}{bean.<caret>field}
            </div>
        """)

        myFixture.addClass("""
            package com.test;

            public class TestClass {
                public String field;
            }
        """)

        val reference = myFixture.file.findReferenceAt(myFixture.editor.caretModel.offset)?.resolve()
            ?: AssertionError("Unable to get reference")

        TestCase.assertEquals("PsiField:field", reference.toString())
    }

    fun testReferenceToDeclarationAttribute() {
        myFixture.configureByText("test.html", """
            <div data-sly-use.bean="com.test.TestClass">
                ${'$'}{<caret>bean}
            </div>
        """)
        myFixture.addClass("package com.test; public class TestClass {}")

        val element = myFixture.file.findReferenceAt(myFixture.editor.caretModel.offset)?.resolve()
            ?: AssertionError("Unable to resolve reference")

        assertTrue(element is XmlAttributeValueImpl)

        TestCase.assertEquals("\"com.test.TestClass\"", (element as XmlAttributeValueImpl).text)
    }

}