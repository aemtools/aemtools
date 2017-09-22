package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class HtlVariablesRenameTest : BaseRenameTest() {

  fun testRenameDataSlyUseVariable() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-use.bean="">
                    $DOLLAR{${CARET}bean}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-use.renamed="">
                    $DOLLAR{renamed}
                </div>
            """)
    }
  }

  fun testRenameDataSlyUseVariableFromAttribute() = renameCase {
    before {
      addHtml("test.html", """
                <div ${CARET}data-sly-use.bean="">
                    $DOLLAR{bean}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-use.renamed="">
                    $DOLLAR{renamed}
                </div>
            """)
    }
  }

  fun testRenameDataSlyTestVariable() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-test.test="$DOLLAR{properties}">
                    $DOLLAR{${CARET}test}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-test.renamed="$DOLLAR{properties}">
                    $DOLLAR{renamed}
                </div>
            """)
    }
  }

  fun testRenameDataSlyTestFromAttribute() = renameCase {
    before {
      addHtml("test.html", """
                <div ${CARET}data-sly-test.test="$DOLLAR{properties}">
                    $DOLLAR{test}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-test.renamed="$DOLLAR{properties}">
                    $DOLLAR{renamed}
                </div>
            """)
    }
  }

  fun testRenameDataSlyListMainVariableFromEl() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-list="">
                    $DOLLAR{${CARET}item}
                    $DOLLAR{itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-list.renamed="">
                    $DOLLAR{renamed}
                    $DOLLAR{renamedList}
                </div>
            """)
    }
  }

  fun testRenameDataSlyListHelperVariable() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-list="">
                    $DOLLAR{item}
                    $DOLLAR{${CARET}itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-list.renamed="">
                    $DOLLAR{renamed}
                    $DOLLAR{renamedList}
                </div>
            """)
    }
  }

  fun testRenameDataSlyListFromAttribute() = renameCase {
    before {
      addHtml("test.html", """
                <div ${CARET}data-sly-list="">
                    $DOLLAR{item} $DOLLAR{itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-list.renamed="">
                    $DOLLAR{renamed} $DOLLAR{renamedList}
                </div>
            """)
    }
  }

  fun testRenameDataSlyRepeatMainVariableFromEl() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-repeat="">
                    $DOLLAR{${CARET}item} $DOLLAR{itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-repeat.renamed="">
                    $DOLLAR{renamed} $DOLLAR{renamedList}
                </div>
            """)
    }
  }

  fun testRenameDataSlyRepeatHelperFromEl() = renameCase {
    before {
      addHtml("test.html", """
                <div data-sly-repeat="">
                    $DOLLAR{item} $DOLLAR{${CARET}itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-repeat.renamed="">
                    $DOLLAR{renamed} $DOLLAR{renamedList}
                </div>
            """)
    }
  }

  fun testRenameDataSlyRepeatFromAttribute() = renameCase {
    before {
      addHtml("test.html", """
                <div ${CARET}data-sly-repeat="">
                    $DOLLAR{item} $DOLLAR{itemList}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addHtml("test.html", """
                <div data-sly-repeat.renamed="">
                    $DOLLAR{renamed} $DOLLAR{renamedList}
                </div>
            """)
    }
  }

}
