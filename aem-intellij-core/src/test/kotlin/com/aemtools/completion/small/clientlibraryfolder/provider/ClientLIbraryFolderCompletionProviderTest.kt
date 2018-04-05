package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [ClientLibraryFolderCompletionProvider].
 *
 * @author Dmytro Primshyts
 */
class ClientLIbraryFolderCompletionProviderTest : CompletionBaseLightTest() {

  fun testFullCompletion() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:ClientLibraryFolder" $CARET />
    """)
    shouldContain(
        "channels", "categories", "dependencies",
        "embed"
    )
  }

  fun testCompletionShouldFilterOutAlreadyPresentVariants() = completionTest {
    addXml(".content.xml", """
       <jcr:root jcr:primaryType="cq:ClientLibraryFolder" embed="[blah]" $CARET />
    """)

    shouldContain("channels",
        "categories", "dependencies")
    shouldNotContain("embed")
  }

}
