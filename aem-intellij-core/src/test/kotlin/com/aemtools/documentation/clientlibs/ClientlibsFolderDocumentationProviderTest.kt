package com.aemtools.documentation.clientlibs

import com.aemtools.common.constant.const
import com.aemtools.test.documentation.BaseDocumentationTest
import com.aemtools.test.fixture.clientLibrary

/**
 * Test for [ClientlibsFolderDocumentationProvider].
 *
 * @author Kostiantyn Diachenko
 */
class ClientlibsFolderDocumentationProviderTest
  : BaseDocumentationTest(ClientlibsFolderDocumentationProvider()) {

  fun `test documentation for category in categories property`() = docCase {
    clientLibrary("/${const.JCR_ROOT}/myapp/comp1/clientlib/.content.xml",
        listOf("category1", "${CARET}category2"),
        listOf("dep1", "dep2"),
        listOf("embed1", "embed2"),
        listOf("mobile", "tablet")
    )
    clientLibrary("/${const.JCR_ROOT}/myapp/comp2/clientlib/.content.xml",
        listOf("category2", "category3", "category4"),
        listOf("dep3", "dep4"),
        listOf("embed3", "embed4"),
        listOf("mobile", "tablet")
    )

    documentation("""
        <html><head></head><body>
        <h2>Category: category2</h2>
        <h2>Declared in:</h2>
        <a href="/src/jcr_root/myapp/comp2/clientlib/.content.xml">
        /myapp/comp2/clientlib/.content.xml
        </a><br/>
        <a href="/src/jcr_root/myapp/comp1/clientlib/.content.xml">
        /myapp/comp1/clientlib/.content.xml
        </a><br/>
        <h2>
        Depends on:
        </h2>
        <ul>
        <li>dep3</li>
        <li>dep4</li>
        <li>dep1</li>
        <li>dep2</li>
        </ul>
        <h2>Embeds:</h2>
        <ul>
        <li>embed3</li>
        <li>embed4</li>
        <li>embed1</li>
        <li>embed2</li>
        </ul>
        </body>
        </html>
    """.trimIndent())
  }

  fun `test documentation for category in embed property`() = docCase {
    clientLibrary("/${const.JCR_ROOT}/myapp/comp1/clientlib/.content.xml",
        listOf("category1", "category2"),
        listOf("dep1", "dep2"),
        listOf("embed.${CARET}category", "embed2"),
        listOf("mobile", "tablet")
    )
    clientLibrary("/${const.JCR_ROOT}/myapp/comp2/clientlib/.content.xml",
        listOf("embed.category", "category3", "category4"),
        listOf("dep3", "dep4"),
        listOf("embed3", "embed4"),
        listOf("mobile", "tablet")
    )

    documentation("""
        <html><head></head><body>
        <h2>Category: embed.category</h2>
        <h2>Declared in:</h2>
        <a href="/src/jcr_root/myapp/comp2/clientlib/.content.xml">
        /myapp/comp2/clientlib/.content.xml
        </a><br/>
        <h2>
        Depends on:
        </h2>
        <ul>
        <li>dep3</li>
        <li>dep4</li>
        </ul>
        <h2>Embeds:</h2>
        <ul>
        <li>embed3</li>
        <li>embed4</li>
        </ul>
        </body>
        </html>
    """.trimIndent())
  }

  fun `test documentation for category in dependencies property`() = docCase {
    clientLibrary("/${const.JCR_ROOT}/myapp/comp1/clientlib/.content.xml",
        listOf("category1", "category2"),
        listOf("dep1", "dep.${CARET}category"),
        listOf("embed1", "embed2"),
        listOf("mobile", "tablet")
    )
    clientLibrary("/${const.JCR_ROOT}/myapp/comp2/clientlib/.content.xml",
        listOf("category2", "category3", "dep.category"),
        listOf("dep3", "dep4"),
        listOf("embed3", "embed4"),
        listOf("mobile", "tablet")
    )

    documentation("""
        <html><head></head><body>
        <h2>Category: dep.category</h2>
        <h2>Declared in:</h2>
        <a href="/src/jcr_root/myapp/comp2/clientlib/.content.xml">
        /myapp/comp2/clientlib/.content.xml
        </a><br/>
        <h2>
        Depends on:
        </h2>
        <ul>
        <li>dep3</li>
        <li>dep4</li>
        </ul>
        <h2>Embeds:</h2>
        <ul>
        <li>embed3</li>
        <li>embed4</li>
        </ul>
        </body>
        </html>
    """.trimIndent())
  }
}
