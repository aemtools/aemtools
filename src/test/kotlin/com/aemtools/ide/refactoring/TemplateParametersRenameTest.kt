package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest
import com.aemtools.constant.const.JCR_ROOT

/**
 * @author Dmytro Troynikov
 */
class TemplateParametersRenameTest : BaseRenameTest() {

  fun testOptionRenameFromDeclaration() = renameCase {
    before {
      addHtml("$JCR_ROOT/myapp/component/component.html", """
                <div data-sly-template.template="$DOLLAR{@ ${CARET}param}">
                    $DOLLAR{param @ context=param}
                </div>
            """)
      addHtml("$JCR_ROOT/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.template @ param='value'}"></div>
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("$JCR_ROOT/myapp/component/component.html", """
                <div data-sly-template.template="$DOLLAR{@ renamed}">
                    $DOLLAR{renamed @ context=renamed}
                </div>
            """)
      addHtml("$JCR_ROOT/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.template @ renamed='value'}"></div>
                </div>
            """)
    }
  }

  fun testOptionRenameFromLocalUsage() = renameCase {
    before {
      addHtml("$JCR_ROOT/myapp/component/component.html", """
                <div data-sly-template.template="$DOLLAR{ @ param}">
                    $DOLLAR{${CARET}param}
                </div>
            """)
      addHtml("$JCR_ROOT/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.template @ param='value'}"></div>
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("$JCR_ROOT/myapp/component/component.html", """
                <div data-sly-template.template="$DOLLAR{ @ renamed}">
                    $DOLLAR{renamed}
                </div>
            """)
      addHtml("$JCR_ROOT/myapp/component/other.html", """
                <div data-sly-use.template="component.html">
                    <div data-sly-call="$DOLLAR{template.template @ renamed='value'}"></div>
                </div>
            """)
    }
  }

  //FIXME will be fixed in separate task
  /*
  fun testOptionRenameFromOuterUsage() = renameCase {
      before {
          addHtml("$JCR_ROOT/myapp/component/component.html", """
              <div data-sly-template.template="$DOLLAR{ @ param}">
                  $DOLLAR{param}
              </div>
          """)
          addHtml("$JCR_ROOT/myapp/component/other.html", """
              <div data-sly-use.template="component.html">
                  <div data-sly-call="$DOLLAR{template.template @ ${CARET}param='value'}"></div>
              </div>
          """)
      }
      renameTo("renamed")
      after {
          addHtml("$JCR_ROOT/myapp/component/component.html", """
              <div data-sly-template.template="$DOLLAR{ @ renamed}">
                  $DOLLAR{renamed}
              </div>
          """)
          addHtml("$JCR_ROOT/myapp/component/other.html", """
              <div data-sly-use.template="component.html">
                  <div data-sly-call="$DOLLAR{template.template @ renamed='value'}"></div>
              </div>
          """)
      }
  }
  */

}
