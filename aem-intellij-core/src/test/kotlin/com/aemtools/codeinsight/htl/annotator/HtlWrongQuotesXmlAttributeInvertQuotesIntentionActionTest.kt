package com.aemtools.codeinsight.htl.annotator

import com.aemtools.codeinsight.htl.intention.HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction
import com.aemtools.test.junit.MockitoExtension
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock

/**
 * Test for [HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction].
 *
 * @author Dmytro Primshyts
 */
@ExtendWith(MockitoExtension::class)
internal class HtlWrongQuotesXmlAttributeInvertQuotesIntentionActionTest {

  @Mock lateinit var xmlAttribute: XmlAttribute
  @Mock lateinit var pointer: SmartPsiElementPointer<XmlAttribute>

  lateinit var tested: HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction

  @BeforeEach
  fun init() {
    tested = HtlWrongQuotesXmlAttributeInvertQuotesIntentionAction(pointer)
  }

  @Test
  fun `test format`() {
    assertThat(tested.text)
        .isEqualTo("Invert XML Attribute quotes")

    assertThat(tested.familyName)
        .isEqualTo("HTL Intentions")
  }

}
