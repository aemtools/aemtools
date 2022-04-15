package com.aemtools.documentation.htl

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.documentation.BaseDocumentationTest
import com.aemtools.test.fixture.clientLibrary

/**
 * Test for [HtlELDocumentationProvider].
 *
 * @author Dmytro Primshyts
 */
class HtlElDocumentationCategoriesValueTest :
    BaseDocumentationTest(HtlELDocumentationProvider()) {

  fun testDoc() = docCase {
    clientLibrary("/$JCR_ROOT/myapp/comp/clientlib/.content.xml",
        listOf("category1", "category2"),
        listOf("dep1", "dep2"),
        listOf("embed1", "embed2"),
        listOf("mobile", "tablet")
    )

    addHtml("/$JCR_ROOT/myapp/comp2/comp2.html", """
        <div data-sly-use.cl="/libs/granite/sightly/templates/clientlib.html"></div>
        <div data-sly-call="$DOLLAR{cl.all @ categories='cate${CARET}gory1'}
    """.trimIndent())

    documentation("""
        <html><head></head><body>
        <h2>Declared in:</h2>
        <a href="/src/jcr_root/myapp/comp/clientlib/.content.xml">
        /src/jcr_root/myapp/comp/clientlib/.content.xml
        </a>
        <h2>
        Depends on:
        </h2>
        <ul>
        <li>dep1</li>
        <li>dep2</li>
        </ul><h2>Embeds:</h2><ul><li>embed1</li><li>embed2</li></ul></body></head>
    """.trimIndent())
  }

}
