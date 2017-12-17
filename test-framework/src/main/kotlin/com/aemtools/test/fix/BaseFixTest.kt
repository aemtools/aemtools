package com.aemtools.test.fix

import com.aemtools.test.base.BaseLightTest
import com.intellij.codeInspection.LocalInspectionTool
import junit.framework.TestCase
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Troynikov
 */
abstract class BaseFixTest : BaseLightTest() {

  fun fixTest(fixDsl: QuickFixDsl.() -> Unit) {
    val fix = QuickFixDsl().apply(fixDsl)

    myFixture.configureByText(
        fix.before!!.name,
        fix.before!!.content
    )

    myFixture.enableInspections(
        fix.inspection!!
    )

    val intentionAction = myFixture
        .getAvailableIntention(fix.fixName!!)

    TestCase.assertNotNull(intentionAction)

    myFixture.launchAction(intentionAction!!)


    myFixture.checkResult(fix.after!!.content)

  }

}

data class FileDescriptor(
    val name: String,
    val content: String
)

class QuickFixDsl {

  var inspection: Class<out LocalInspectionTool>? = null
  var fixName: String? = null
  var before: FileDescriptor? = null

  var after: FileDescriptor? = null

  fun html(name: String, @Language("HTML") text: String) =
      FileDescriptor(name, text)

}
