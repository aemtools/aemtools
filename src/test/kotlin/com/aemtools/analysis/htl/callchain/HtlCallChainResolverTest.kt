package com.aemtools.analysis.htl.callchain

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtlFile
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.PropertyAccessMixin
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import junit.framework.TestCase

/**
 * @author Dmytro_Troynikov
 */
class HtlCallChainResolverTest : LightCodeInsightFixtureTestCase() {

    fun testEmptyChain() {
        myFixture.configureByText("test.html", """
            <div data-sly-use.bean="com.test.UnknownModel">
                <div data-sly-test.test="${'$'}{bean.unknownField}">
                    ${'$'}{test}
                </div>
            </div>
        """)

        val file = myFixture.file.getHtlFile()
                ?: throw AssertionError("Unable to obtain HTL psi from ${myFixture.file.text}")

        val els = file.findChildrenByClass(HtlHtlEl::class.java)

        val lastElement = els.lastOrNull()
                ?: throw AssertionError("Unable to find el in ${file.text}")

        val propertyAccess = lastElement.findChildrenByType(PropertyAccessMixin::class.java)
                .firstOrNull() ?: throw AssertionError("Unable to get propertyAccess from ${lastElement.text}")

        val chain = propertyAccess.accessChain()
                ?: throw AssertionError("Unable to extract access chain from ${propertyAccess.text}")

        TestCase.assertEquals(3, chain.callChainSegments.size)
    }

}