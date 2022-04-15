package com.aemtools.completion.small.clientlibraryfolder.provider

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Test for [ValueOfCategoriesCompletionProvider].
 *
 * @author Dmytro Primshyts
 */
class ValueOfCategoriesCompletionProviderTest : CompletionBaseLightTest(false) {

  fun testCategories() = completionTest {
    addXml(".content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[$CARET]" />
    """)

    addXml("other/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[first, second]" />
    """)

    addXml("cl2/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[third]" />
    """)

    shouldContain(
        "first",
        "second",
        "third"
    )
  }

  fun testCategoriesShouldFilterOutDuplicates() = completionTest {
    addXml(".content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[first, $CARET, third]" />
    """)

    addXml("other/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[first, second]" />
    """)

    addXml("cl2/.content.xml", """
      <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
          categories="[third]" />
    """)

    shouldContain(
        "second"
    )
  }

}
