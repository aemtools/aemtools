package com.aemtools.completion.htl.provider.option

import com.aemtools.common.constant.const
import com.aemtools.test.completion.CompletionBaseLightTest
import com.aemtools.test.completion.model.ICompletionTestFixture

/**
 * Test for [HtlClientLibraryTemplateCategoryCompletionProvider].
 *
 * @author Dmytro Primshyts
 */
class HtlClientLibraryTemplateCategoryCompletionProviderTest : CompletionBaseLightTest(false) {

  fun testForJs() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.js @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForCss() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.css @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForAll() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.all @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForJsViaArray() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.js @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForCssViaArray() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.css @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForAllViaArray() = completionTest {
    addClientlibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.all @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  private fun ICompletionTestFixture.addClientlibrary(
      fileName: String,
      categories: List<String>,
      embeds: List<String> = emptyList(),
      channels: List<String> = emptyList()
  ) {
    addXml(fileName, """
        <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
             categories="$categories"
             embeds="$embeds"
             channels="$channels"
    """.trimIndent())
  }

}
