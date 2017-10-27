package com.aemtools.lang.clientlib.reference

import com.aemtools.blocks.reference.BaseReferenceTest
import com.intellij.psi.PsiFile

/**
 * @author Dmytro_Troynikov
 */
class CdReferenceTest : BaseReferenceTest() {

  fun testReferenceToFileInSameDir() = testReference {
    addFile("component/js.txt", "${CARET}file.js")
    addFile("component/file.js", "included")
    shouldResolveTo(PsiFile::class.java)
    shouldContainText("included")
  }

  fun testReferenceInSubdirectory() = testReference {
    addFile("component/js.txt", "${CARET}js/file.js")
    addFile("component/js/file.js", "included")
    shouldResolveTo(PsiFile::class.java)
    shouldContainText("included")
  }

  fun testReferenceInSubdirectoryWithBasePath() = testReference {
    addFile("component/js.txt", """
            #base=js
            ${CARET}file.js
        """.trimIndent())
    addFile("component/js/file.js", "included")
    shouldResolveTo(PsiFile::class.java)
    shouldContainText("included")
  }

  fun testReferenceToParentDirectory() = testReference {
    addFile("component/js.txt", "$CARET../file.js")
    addFile("file.js", "included")
    shouldResolveTo(PsiFile::class.java)
    shouldContainText("included")
  }

}
