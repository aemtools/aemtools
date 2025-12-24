package com.aemtools.inspection.html.fix

import com.aemtools.test.util.mock
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.`when`
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

/**
 * Specification for [SubstituteWithRawAttributeIntentionAction].
 * @author Dmytro Primshyts
 */
object SubstituteWithRawAttributeIntentionActionSpec : ShouldSpec({
  val xmlAttributePointer: SmartPsiElementPointer<XmlAttribute> = mock()
  val xmlAttribute: XmlAttribute = mock()
  val tested =
    SubstituteWithRawAttributeIntentionAction(
      xmlAttributePointer,
      "Test message"
    )


  context("style check") {

    should("have correct family") {
      assertThat(tested.familyName)
        .isEqualTo("HTL Intentions")
    }

    should("have correct text") {
      assertThat(tested.text)
        .isEqualTo("Test message")
    }
  }

  context("invoke") {
    val project: Project = mock()

    beforeEach {
      `when`(xmlAttributePointer.element)
        .thenReturn(xmlAttribute)
    }

    should("ignore if no element available") {
      `when`(xmlAttributePointer.element)
        .thenReturn(null)

      tested.invoke(project, null, null)

      verify(xmlAttribute, never())
        .name
    }

    should("rename attribute if it available") {
      `when`(xmlAttribute.name)
        .thenReturn("data-sly-attribute.style")

      tested.invoke(project, null, null)

      verify(xmlAttribute)
        .name = "style"
    }
  }

})
