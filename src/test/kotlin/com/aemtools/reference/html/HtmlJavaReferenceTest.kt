package com.aemtools.reference.html

import com.intellij.psi.PsiClass
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.TestCase

/**
 * @author Dmytro_Troynikov
 */
class HtmlJavaReferenceTest : LightCodeInsightFixtureTestCase() {

    fun testSlyUseClassReferencesToJava() {

        myFixture.configureByText("test.html", """
            <div data-sly-use.bean="com.test.TestClass<caret>"></div>
        """)

        myFixture.addClass("""
            package com.test;
            public class TestClass {}
        """)

        val classReference = myFixture.file.findReferenceAt(myFixture.editor.caretModel.offset)
                ?: throw AssertionError("No reference found in ${myFixture.file.text}")
        val psiClass = classReference.resolve()
                ?: throw AssertionError("Unable to resolve $classReference")

        TestCase.assertEquals("com.test.TestClass",
                (psiClass as PsiClass).qualifiedName)
    }

}