package com.aemtools.inspection.html.fix

import com.aemtools.test.util.memo
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for [SubstituteWithRawAttributeIntentionAction].
 * @author Dmytro Troynikov
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
      whenever(xmlAttributePointer.element)
          .thenReturn(xmlAttribute)
    }

    it("should ignore if no element available") {
      whenever(xmlAttributePointer.element)
          .thenReturn(null)

      tested.invoke(project, null, null)

      verify(xmlAttribute, never())
          .name
    }

    it("should rename attribute if it available") {
      whenever(xmlAttribute.name)
          .thenReturn("data-sly-attribute.style")

      tested.invoke(project, null, null)

      verify(xmlAttribute)
          .name = "style"
    }
  }

})
