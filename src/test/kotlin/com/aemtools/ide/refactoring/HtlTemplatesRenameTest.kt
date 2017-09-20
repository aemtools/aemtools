package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest
import com.aemtools.constant.const

/**
 * @author Dmytro Troynikov
 */
class HtlTemplatesRenameTest : BaseRenameTest() {

  fun testRenameTemplateFromAttribute() = renameCase {
    before {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div ${CARET}data-sly-template.myTemplate=""></div>
                <div data-sly-call="$DOLLAR{myTemplate}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.myTemplate}"></div>
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div data-sly-template.renamed=""></div>
                <div data-sly-call="$DOLLAR{renamed}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.renamed}"></div>
                </div>
            """)
    }
  }

  fun testRenameTemplateFromLocalUsage() = renameCase {
    before {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div data-sly-template.myTemplate=""></div>
                <div data-sly-call="$DOLLAR{${CARET}myTemplate}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.myTemplate}"></div>
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div data-sly-template.renamed=""></div>
                <div data-sly-call="$DOLLAR{renamed}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.renamed}"></div>
                </div>
            """)
    }
  }

  fun testRenameTemplateFromOuterUsage() = renameCase {
    before {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div data-sly-template.myTemplate=""></div>
                <div data-sly-call="$DOLLAR{myTemplate}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.${CARET}myTemplate}"></div>
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("${const.JCR_ROOT}/myapp/component/component.html", """
                <div data-sly-template.renamed=""></div>
                <div data-sly-call="$DOLLAR{renamed}"></div>
            """)
      addHtml("${const.JCR_ROOT}/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.renamed}"></div>
                </div>
            """)
    }
  }

}
