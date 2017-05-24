package com.aemtools.completion.widget

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.completion.widget.WidgetVariantsProvider.Companion.DEFAULT_ATTRIBUTES
import com.aemtools.completion.widget.WidgetVariantsProvider.Companion.JCR_PRIMARY_TYPE_VALUES

/**
 * @author Dmytro_Troynikov
 */
class ClassicWidgetCompletionTest : CompletionBaseLightTest() {

    fun testAttributeWithXTypeUnknown() = completionTest {
        addXml("dialog.xml", """
            <item
                $CARET>
        """.inJcrRoot())
        shouldContain(DEFAULT_ATTRIBUTES)
    }

    fun testJcrPrimaryTypeValues() = completionTest {
        addXml("dialog.xml", """
            <item jcr:primaryType="$CARET"
        """.inJcrRoot())
        shouldContain(JCR_PRIMARY_TYPE_VALUES)
    }

    fun testSuggestTheFieldsResolvedFromXType() = completionTest {
        addXml("dialog.xml", """
            <item xtype="pathfield"
                $CARET
            >
        """.inJcrRoot())
        shouldContain(listOf("label", "name"), false)
    }

    private fun String.inJcrRoot() : String = """
        <?xml version="1.0" encoding="UTF-8"?>
        <jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0"
                  xmlns:jcr="http://www.jcp.org/jcr/1.0">
        $this
        </jcr:root>
    """

}