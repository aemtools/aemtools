package com.aemtools.completion.htl.provider.option

import com.aemtools.common.constant.const
import com.aemtools.test.completion.CompletionBaseLightTest
import com.aemtools.test.fixture.ClientlibraryMixin
import com.aemtools.test.fixture.clientLibrary

/**
 * Test for [HtlClientLibraryTemplateCategoryCompletionProvider].
 *
 * @author Dmytro Primshyts
 */
class HtlClientLibraryTemplateCategoryCompletionProviderTest : CompletionBaseLightTest(false),
    ClientlibraryMixin {

  fun testForJs() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.js @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForCss() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.css @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForAll() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.all @ categories='$CARET'}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForJsViaArray() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.js @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForCssViaArray() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.css @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

  fun testForAllViaArray() = completionTest {
    clientLibrary("/${const.JCR_ROOT}/apps/myapp/.content.xml", listOf("lib1", "lib2"))

    addHtml("${const.JCR_ROOT}/apps/myapp/myapp.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"> </div>

        <div data-sly-call="$DOLLAR{cl.all @ categories=['$CARET']}
    """.trimIndent())

    shouldContain("lib1", "lib2")
  }

}
