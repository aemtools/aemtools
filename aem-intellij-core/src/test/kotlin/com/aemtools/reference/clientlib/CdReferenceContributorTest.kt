package com.aemtools.reference.clientlib

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.reference.BaseReferenceTest
import com.intellij.psi.PsiFile

/**
 * Tests for [CdReferenceContributor].
 *
 * @author Kostiantyn Diachenko
 */
class CdReferenceContributorTest : BaseReferenceTest() {
  fun `test reference for clientlib file with relative #base path`() = testReference {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/clientlibs/js
            ${CARET}first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "opened")

    shouldResolveTo(PsiFile::class.java)
    shouldContainText("opened")
  }

  fun `test reference for clientlib file with absolute #base path`() = testReference {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1
            ${CARET}clientlibs/js/first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "opened")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")

    shouldResolveTo(PsiFile::class.java)
    shouldContainText("opened")
  }

  fun `test reference for clientlib file with several files under absolute #base path`() = testReference {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1
            clientlibs/js/first.js
            ${CARET}inner/clientlibs/js/second.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "opened")

    shouldResolveTo(PsiFile::class.java)
    shouldContainText("opened")
  }

  fun `test js reference for clientlib file under absolute #base path below another section with #base path`() =
      testReference {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1
            clientlibs/js/first.js
            
            #base=/apps/components/path2
            ${CARET}clientlibs/js/second.js
        """)
        addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
        addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "opened")

        shouldResolveTo(PsiFile::class.java)
        shouldContainText("opened")
      }

  fun `test css reference for clientlib file under absolute #base path below another section with #base path`() =
      testReference {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1
            clientlibs/css/first.css
            
            #base=/apps/components/path2
            ${CARET}clientlibs/css/second.css
        """)
        addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
        addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.css", "opened")

        shouldResolveTo(PsiFile::class.java)
        shouldContainText("opened")
      }

  fun `test reference for clientlib file with several files under dot #base path`() = testReference {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js.txt", """
            #base=.
            ${CARET}first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/first.js", "opened")

    shouldResolveTo(PsiFile::class.java)
    shouldContainText("opened")
  }

  fun `test reference for clientlib file with several files under container file dir #base path`() = testReference {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js.txt", """
            #base=./js
            ${CARET}first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "opened")

    shouldResolveTo(PsiFile::class.java)
    shouldContainText("opened")
  }
}
