package com.aemtools.completion.clientlib

import com.aemtools.common.constant.const.JCR_ROOT
import com.aemtools.reference.clientlib.CdReferenceContributor
import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * Tests for [CdCompletionContributor] with js files.
 *
 * @author Dmytro Primshyts
 */
class ClientlibJsTxtCompletionTest : CompletionBaseLightTest(false) {
  fun `test js completion`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")
    shouldContain(listOf(
        "first.js",
        "js/second.js"
    ))
  }

  fun `test completion with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")
    shouldContain(listOf(
        "first.js",
        "second.js"
    ))
  }

  fun `test filter out duplicates without any #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            first.js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.js", "")
    shouldContain(listOf("second.js"))
    shouldNotContain(listOf("first.js"))
  }

  fun `test filter out duplicates with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            js/first.js
            #base=js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")
    shouldContain(listOf("second.js"))
    shouldNotContain(listOf("first.js"))
  }

  fun `test filter out duplicates after caret without any #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
          $CARET
          first.js
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/second.js", "")

    shouldContain(listOf("second.js"))
    shouldNotContain(listOf("first.js"))
  }

  fun `test filter out duplicates after caret with #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
          $CARET
          #base=js
          first.js
    """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/second.js", "")

    shouldContain(listOf("js/second.js"))
    shouldNotContain(listOf(
        "js/first.js",
        "first.js"
    ))
  }

  fun `test #base path completion under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/file.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/css/file.css", "")
    shouldContain(listOf("js"))
    shouldNotContain(listOf("css"))
  }

  fun `test #base path completion under containing file dir with different level directories`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=$CARET
        """)
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js/inner/second.js", "")
    shouldContain(listOf(
        "js",
        "js/inner"
    ))
  }

  fun `test absolute #base path completion starting with root level`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf("apps/components/path1/clientlibs/js", "apps/components/path2/clientlibs/js"))
  }

  fun `test absolute #base path completion starting with 3rd level path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/js/third.js", "")
    shouldContain(listOf(
        "path1/clientlibs/js",
        "path1/inner/clientlibs/js",
        "path2/inner/clientlibs/js"
    ))
  }

  fun `test absolute #base path completion starting with 5rd level path without matched files by typed character`() =
      completionTest {
        addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/a$CARET
        """)
        addFile("$JCR_ROOT/apps/components/zero.js", "")
        addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
        shouldContain(emptyList())
      }

  fun `test absolute #base path completion starting with 5rd level path with matched files by typed character`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/s$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    shouldContain(listOf("js"))
  }

  fun `test relative #base path completion with -1 path level`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/js/third.js", "")
    shouldContain(emptyList())
  }

  fun `test relative #base path completion with -2 path levels more globally`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../a$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/js/third.js", "")
    shouldContain(listOf(
        "path1/clientlibs/js",
        "path1/inner/clientlibs/js",
        "path2/inner/clientlibs/js"
    ))
  }

  fun `test relative #base path completion with -3 path levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../../c$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/js/third.js", "")
    shouldContain(listOf(
        "components",
        "components/path1/clientlibs/js",
        "components/path1/inner/clientlibs/js",
        "components/path2/inner/clientlibs/js"
    ))
  }

  fun `test relative #base path completion with -2 path levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js.txt", """
            #base=../../j$CARET
        """)
    addFile("$JCR_ROOT/apps/components/zero.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    addFile("$JCR_ROOT/apps/components/path2/inner/clientlibs/js/third.js", "")
    shouldContain(listOf(
        "clientlibs/js",
        "inner/clientlibs/js"
    ))
  }

  fun `test completion with absolute #base path with 3 levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf(
        "clientlibs/js/first.js"
    ))
  }

  fun `test completion with absolute #base path with 2 levels`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf(
        "path1/clientlibs/js/first.js",
        "path2/clientlibs/js/second.js"
    ))
  }

  fun `test filter out duplicates defined under 1 absolute #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/js
            first.js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/second.js", "")
    shouldContain(listOf("second.js"))
    shouldNotContain(listOf("first.js"))
  }

  fun `test filter out duplicates with absolute #base path and another #base path above`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1/clientlibs/js
            first.js
            
            #base=/apps/components
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf("path2/clientlibs/js/second.js"))
    shouldNotContain(listOf("path1/clientlibs/js/first.js"))
  }

  fun `test filter out duplicates with absolute #base path and another #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components
            $CARET
            
            #base=/apps/components/path1/clientlibs/js
            first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf("path2/clientlibs/js/second.js"))
    shouldNotContain(listOf("path1/clientlibs/js/first.js"))
  }

  fun `test completion with relative #base path with -2 levels and 1 subdir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldContain(listOf(
        "clientlibs/js/first.js",
        "inner/clientlibs/js/second.js"
    ))
  }

  fun `test completion with relative #base path with -2 levels and 3 subdirs`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/clientlibs/js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path2/clientlibs/js/second.js", "")
    shouldContain(listOf("first.js"))
  }

  fun `test filter out duplicates defined under 1 relative #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1/clientlibs/js
            first.js
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/second.js", "")
    shouldContain(listOf("second.js"))
    shouldNotContain(listOf("first.js"))
  }

  fun `test filter out duplicates with relative #base path and another #base path above`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1
            clientlibs/js/first.js
            
            #base=../../path1
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldNotContain(listOf("clientlibs/js/first.js"))
    shouldContain(listOf("inner/clientlibs/js/second.js"))
  }

  fun `test filter out duplicates with relative #base path and another #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1
            $CARET
            
            #base=../../path1
            clientlibs/js/first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldNotContain(listOf("clientlibs/js/first.js"))
    shouldContain(listOf("inner/clientlibs/js/second.js"))
  }

  fun `test filter out duplicates with relative #base path and another absolute #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=../../path1
            $CARET
            
            #base=/apps/components/path1/clientlibs/js
            first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldNotContain(listOf("clientlibs/js/first.js"))
    shouldContain(listOf("inner/clientlibs/js/second.js"))
  }

  fun `test filter out duplicates with absolute #base path and another relative #base path below`() = completionTest {
    addFile("$JCR_ROOT/apps/components/comp/clientlibs/js.txt", """
            #base=/apps/components/path1
            $CARET
            
            #base=../../path1/clientlibs/js
            first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldNotContain(listOf("clientlibs/js/first.js"))
    shouldContain(listOf("inner/clientlibs/js/second.js"))
  }

  fun `test filter out duplicates with abs #base path and #base path under containing file dir`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js.txt", """
            #base=/apps/components/path1
            $CARET
            
            #base=js
            first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldNotContain(listOf("clientlibs/js/first.js"))
    shouldContain(listOf("inner/clientlibs/js/second.js"))
  }

  fun `test completion with dot #base path `() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js.txt", """
            #base=.
            $CARET
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/inner/clientlibs/js/second.js", "")
    shouldContain(listOf("js/first.js"))
  }

  fun `test filter out duplicates with abs #base path and dot #base path`() = completionTest {
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js.txt", """
            #base=.
            $CARET
            
            #base=js
            first.js
        """)
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/first.js", "")
    addFile("$JCR_ROOT/apps/components/path1/clientlibs/js/second.js", "")
    shouldNotContain(listOf("js/first.js"))
    shouldContain(listOf("js/second.js"))
  }
}
