package com.aemtools.inspection.html.fix

import com.aemtools.test.util.memo
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito.`when`
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

/**
 * Specification for [SubstituteWithRawAttributeIntentionAction].
 * @author Dmytro Primshyts
 */
object SubstituteWithRawAttributeIntentionActionSpec : Spek({
  val xmlAttributePointer: SmartPsiElementPointer<XmlAttribute> by memo()
  val xmlAttribute: XmlAttribute by memo()
  val tested by memo {
    SubstituteWithRawAttributeIntentionAction(
        xmlAttributePointer,
        "Test message"
    )
  }

  on("style check") {

    it("should have correct family") {
      assertThat(tested.familyName)
          .isEqualTo("HTL Intentions")
    }

    it("should have correct text") {
      assertThat(tested.text)
          .isEqualTo("Test message")
    }
  }

  describe("invoke") {
    val project: Project by memo()

    beforeEachTest {
      `when`(xmlAttributePointer.element)
          .thenReturn(xmlAttribute)
    }

    it("should ignore if no element available") {
      `when`(xmlAttributePointer.element)
          .thenReturn(null)

      tested.invoke(project, null, null)

      verify(xmlAttribute, never())
          .name
    }

    it("should rename attribute if it available") {
      `when`(xmlAttribute.name)
          .thenReturn("data-sly-attribute.style")

      tested.invoke(project, null, null)

      verify(xmlAttribute)
          .name = "style"
    }
  }

})
