package com.aemtools.test.fix

import com.aemtools.common.util.writeCommand
import com.aemtools.test.base.BaseLightTest
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.openapi.application.runWriteAction
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

  fun annotationFixTest(annotatorFixDsl: AnnotatorFixDsl.() -> Unit) {
    val fix = AnnotatorFixDsl().apply(annotatorFixDsl)

    myFixture.configureByText(
        fix.before!!.name,
        fix.before!!.content
    )

    val quickFix = myFixture.getAllQuickFixes(fix.before!!.name)
        .find { it.text == fix.fixName }
        ?: throw AssertionError("Unable to find quick fix with name: ${fix.fixName}")

    writeCommand(project) {
      quickFix.invoke(project, editor, myFixture.file)
    }

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

class AnnotatorFixDsl {

  var fixName: String? = null

  var before: FileDescriptor? = null
  var after: FileDescriptor? = null

  fun html(name: String, @Language("HTML") text: String) =
      FileDescriptor(name, text)

}
