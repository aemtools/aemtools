package com.aemtools.ide.refactoring

import com.aemtools.common.constant.Const
import com.aemtools.test.rename.BaseRenameTest

/**
 * Tests for [com.aemtools.lang.clientlib.manipulator.CdIncludeManipulator].
 *
 * @author Kostiantyn Diachenko
 */
class CdIncludeRenameTest : BaseRenameTest() {

  fun `test should rename cd simple include`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js/second.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            ${CARET}first.js
            js/second.js
      """)
    }
    renameTo("renamed-first.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/renamed-first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js/second.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            renamed-first.js
            js/second.js
      """)
    }
  }

  fun `test should rename cd simple include into directory`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js/second.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            first.js
            js/${CARET}second.js
      """)
    }
    renameTo("renamed-second.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js/renamed-second.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            first.js
            js/renamed-second.js
      """)
    }
  }

  fun `test should rename cd include into directory with relative base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/inner/clientlibs
            ${CARET}js/third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/third.js", "")
    }
    renameTo("renamed-third.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/inner/clientlibs
            js/renamed-third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/renamed-third.js", "")
    }
  }

  fun `test should rename cd include with relative base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/inner/clientlibs/js
            ${CARET}third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/third.js", "")
    }
    renameTo("renamed-third.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/inner/clientlibs/js
            renamed-third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/renamed-third.js", "")
    }
  }

  fun `test should rename cd include into directory with absolute base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components
            ${CARET}path2/inner/clientlibs/js/third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path2/inner/clientlibs/js/third.js", "")
    }
    renameTo("renamed-third.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components
            path2/inner/clientlibs/js/renamed-third.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path2/inner/clientlibs/js/renamed-third.js", "")
    }
  }

  fun `test should rename cd include with absolute base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/js
            first.js
            
            #base=/apps/components
            ${CARET}path2/clientlibs/js/second.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path2/clientlibs/js/second.js", "")
    }
    renameTo("renamed-second.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/js
            first.js
            
            #base=/apps/components
            path2/clientlibs/js/renamed-second.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path2/clientlibs/js/renamed-second.js", "")
    }
  }

  fun `test should rename cd include into directory with dot base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js.txt", """
            #base=.
            ${CARET}js/first.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/second.js", "")
    }
    renameTo("renamed-first.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js.txt", """
            #base=.
            js/renamed-first.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js/renamed-first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path1/inner/clientlibs/js/second.js", "")
    }
  }

  fun `test should rename cd include with dot base path`() = renameCase {
    before {
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js.txt", """
            #base=.
            ${CARET}first.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/second.js", "")
    }
    renameTo("renamed-first.js")
    after {
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/js.txt", """
            #base=.
            renamed-first.js
        """)
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/renamed-first.js", "")
      addFile("${Const.JCR_ROOT}/apps/components/path1/clientlibs/second.js", "")
    }
  }
}
