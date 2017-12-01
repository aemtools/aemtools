package com.aemtools.inspection.html

import com.aemtools.completion.util.toSmartPointer
import com.aemtools.inspection.fix.SubstituteWithRawAttributeIntentionAction
import com.intellij.codeInsight.daemon.impl.analysis.RemoveAttributeIntentionFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.picocontainer.PicoContainer

/**
 * Unit test for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Troynikov
 */
@RunWith(MockitoJUnitRunner::class)
class MessedDataSlyAttributeUnitTest {

  @Mock
  lateinit var attribute: XmlAttribute
  @Mock
  lateinit var holder: ProblemsHolder
  @Mock
  lateinit var project: Project
  @Mock
  lateinit var picoContainer: PicoContainer

  var tested: MessedDataSlyAttributeInspection = MessedDataSlyAttributeInspection()

  @Before
  fun init() {
    `when`(project.picoContainer)
        .thenReturn(picoContainer)
  }

  @Test
  fun testFormat() {
    assertThat(tested.groupDisplayName)
        .isEqualTo("HTL")

    assertThat(tested.displayName)
        .isEqualTo("data-sly-attribute with prohibited attributes")

    assertThat(tested.staticDescription)
        .isEqualTo("""
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>
        """.trimIndent())
  }

  @Test
  fun testStyle() {
    val attr = "style"

    `when`(attribute.name)
        .thenReturn("data-sly-attribute.$attr")

    `when`(attribute.value)
        .thenReturn("\"\${'stub'}\"")

    `when`(attribute.isValid)
        .thenReturn(true)

    `when`(attribute.project)
        .thenReturn(project)

    tested.checkAttribute(attribute, holder, false)

    verify(holder).registerProblem(
        eq(attribute),
        eq("$attr is not allowed in data-sly-attribute"),
        eq(ProblemHighlightType.GENERIC_ERROR_OR_WARNING),
        eq(RemoveAttributeIntentionFix(
            attribute.name, attribute
        )),
        eq(SubstituteWithRawAttributeIntentionAction(
            attribute.toSmartPointer(),
            "Replace with: $attr=\"${attribute.value}\""
        ))
    )
  }

}
