package com.aemtools.completion.clientlib

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Tests for [CdCompletionContributor] with css/less files.
 *
 * @author Dmytro Primshyts
 */
class ClientlibCssTxtCompletionTest : CompletionBaseLightTest(false) {

  fun `test css completion`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "first.css",
        "css/second.less"
    ))
  }

  fun `test completion with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "first.css",
        "second.less"
    ))
  }

  fun `test filter out duplicates without any #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            first.css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun `test filter out duplicates with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            css/first.css
            #base=css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun `test filter out duplicates after caret without any #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
          $CARET
          first.css
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.css", "")

    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun `test filter out duplicates after caret with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
          $CARET
          #base=css
          first.css
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/second.css", "")

    shouldContain(listOf("css/second.css"))
    shouldNotContain(listOf("css/first.css", "first.css"))
  }

  fun `test #base path completion under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/file.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file.css", "")
    shouldContain(listOf("css"))
    shouldNotContain(listOf("js"))
  }

  fun `test #base path completion under containing file dir with different level directories`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file1.css", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/inner/file2.less", "")
    shouldContain(listOf(
        "css",
        "css/inner"
    ))
  }

  fun `test absolute #base path completion starting with root level`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "apps/components/path1/clientlibs/css",
        "apps/components/path2/clientlibs/css"
    ))
  }

  fun `test absolute #base path completion starting with 3rd level path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/css/third.less", "")
    shouldContain(listOf(
        "path1/clientlibs/css",
        "path1/inner/clientlibs/css",
        "path2/inner/clientlibs/css"
    ))
  }

  fun `test absolute #base path completion starting with 5rd level path without matched files by typed character`() =
      completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1/clientlibs/a$CARET
        """)
        addFile("$JCR_ROOT/apps/components/zero.js", "")
        addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
        shouldContain(emptyList())
      }

  fun `test absolute #base path completion starting with 5rd level path with matched files by typed character`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1/clientlibs/s$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    shouldContain(listOf("css"))
  }

  fun `test relative #base path completion with -1 path level`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.less", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/css/third.less", "")
    shouldContain(emptyList())
  }

  fun `test relative #base path completion with -2 path levels more globally`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.less", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/less/third.less", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/less/third.css", "")
    shouldContain(listOf(
        "path1/clientlibs/css",
        "path1/inner/clientlibs/css",
        "path2/inner/clientlibs/less"
    ))
  }

  fun `test relative #base path completion with -3 path levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../../c$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/css/third.less", "")
    shouldContain(listOf(
        "components",
        "components/path1/clientlibs/css",
        "components/path1/inner/clientlibs/css",
        "components/path2/inner/clientlibs/css"
    ))
  }

  fun `test relative #base path completion with -2 path levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css.txt", """
            #base=../../c$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/css/third.css", "")
    shouldContain(listOf(
        "clientlibs/css",
        "inner/clientlibs/css"
    ))
  }

  fun `test completion with absolute #base path with 3 levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.css", "")
    shouldContain(listOf(
        "clientlibs/css/first.css"
    ))
  }

  fun `test completion with absolute #base path with 2 levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.less", "")
    shouldContain(listOf(
        "path1/clientlibs/css/first.css",
        "path2/clientlibs/css/second.less"
    ))
  }

  fun `test filter out duplicates defined under 1 absolute #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1/clientlibs/css
            first.css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun `test filter out duplicates with absolute #base path and another #base path above`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1/clientlibs/css
            first.css
            
            #base=/apps/components
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.css", "")
    shouldContain(listOf("path2/clientlibs/css/second.css"))
    shouldNotContain(listOf("path1/clientlibs/css/first.css"))
  }

  fun `test filter out duplicates with absolute #base path and another #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components
            $CARET
            
            #base=/apps/components/path1/clientlibs/css
            first.less
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.less", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.css", "")
    shouldContain(listOf("path2/clientlibs/css/second.css"))
    shouldNotContain(listOf("path1/clientlibs/css/first.less"))
  }

  fun `test completion with relative #base path with -2 levels and 1 subdir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    shouldContain(listOf(
        "clientlibs/css/first.css",
        "inner/clientlibs/css/second.css"
    ))
  }

  fun `test completion with relative #base path with -2 levels and 3 subdirs`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1/clientlibs/css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/css/second.css", "")
    shouldContain(listOf("first.css"))
  }

  fun `test filter out duplicates defined under 1 relative #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1/clientlibs/css
            first.css
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/second.css", "")
    shouldContain(listOf("second.css"))
    shouldNotContain(listOf("first.css"))
  }

  fun `test filter out duplicates with relative #base path and another #base path above`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1
            clientlibs/css/first.css
            
            #base=../../path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.less", "")
    shouldNotContain(listOf("clientlibs/css/first.css"))
    shouldContain(listOf("inner/clientlibs/css/second.less"))
  }

  fun `test filter out duplicates with relative #base path and another #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1
            $CARET
            
            #base=../../path1
            clientlibs/css/first.css
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    shouldNotContain(listOf("clientlibs/css/first.css"))
    shouldContain(listOf("inner/clientlibs/css/second.css"))
  }

  fun `test filter out duplicates with relative #base path and another absolute #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=../../path1
            $CARET
            
            #base=/apps/components/path1/clientlibs/css
            first.css
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    shouldNotContain(listOf("clientlibs/css/first.css"))
    shouldContain(listOf("inner/clientlibs/css/second.css"))
  }

  fun `test filter out duplicates with absolute #base path and another relative #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css.txt", """
            #base=/apps/components/path1
            $CARET
            
            #base=../../path1/clientlibs/css
            first.css
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.less", "")
    shouldNotContain(listOf("clientlibs/css/first.css"))
    shouldContain(listOf("inner/clientlibs/css/second.less"))
  }

  fun `test filter out duplicates with abs #base path and #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css.txt", """
            #base=/apps/components/path1
            $CARET
            
            #base=css
            first.css
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    shouldNotContain(listOf("clientlibs/css/first.css"))
    shouldContain(listOf("inner/clientlibs/css/second.css"))
  }

  fun `test completion with dot #base path `() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css.txt", """
            #base=.
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/css/second.css", "")
    shouldContain(listOf("css/first.css"))
  }

  fun `test filter out duplicates with abs #base path and dot #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css.txt", """
            #base=.
            $CARET
            
            #base=css
            first.css
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/first.css", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/css/second.css", "")
    shouldNotContain(listOf("css/first.css"))
    shouldContain(listOf("css/second.css"))
  }

}
