package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [JcrTypeCompletionProvider].
 *
 * @author Dmytro Primshyts
 */
class JcrTypeCompletionProviderTest : CompletionBaseLightTest(false) {

  fun testJcrTypeCompletion() = completionTest {
    addXml(".content.xml", """
        <jcr:root jcr:primaryType="cq:ClientLibraryFolder" embed="{$CARET}" />
    """)

    shouldContain(
        "String",
        "Binary",
        "Long",
        "Double",
        "Decimal",
        "Date",
        "Boolean",
        "Name",
        "Path",
        "Reference",
        "WeakReference",
        "URI"
    )
  }

}
