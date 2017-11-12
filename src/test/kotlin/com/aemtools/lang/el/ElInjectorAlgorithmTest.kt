package com.aemtools.lang.el

import com.aemtools.test.base.BaseLightTest.Companion.DOLLAR
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author Dmytro Troynikov
 */
@RunWith(MockitoJUnitRunner::class)
class ElInjectorAlgorithmTest {

  val tested: ElInjector = ElInjector()

  @Mock
  lateinit var registrar: MultiHostRegistrar

  @Mock
  lateinit var context: XmlAttributeValueImpl

  @Mock
  lateinit var containingFile: PsiFile

  @Before
  fun init() {
    `when`(context.containingFile)
        .thenReturn(containingFile)

    `when`(containingFile.name)
        .thenReturn(".content.xml")
  }

  @Test
  fun `should mark single occurrence`() = testInjection(
      "$DOLLAR{}",
      listOf(TextRange.from(1, 3))
  )

  @Test
  fun `should two occurrences`() = testInjection(
      "$DOLLAR{} $DOLLAR{ }",
      listOf(
          TextRange.from(1, 3),
          TextRange.from(5, 4)
      )
  )

  @Test
  fun `should ignore el in string literal`() = testInjection(
      "$DOLLAR{'$DOLLAR{}'}",
      listOf(
          TextRange.from(1, 8)
      )
  )

  private fun testInjection(
      input: String,
      expectedRanges: List<TextRange>
  ) {
    `when`(context.text)
        .thenReturn("\"$input\"")

    tested.getLanguagesToInject(registrar, context)

    if (expectedRanges.isEmpty()) {
      verify(registrar, never())
          .startInjecting(ElLanguage)

      verify(registrar, never())
          .doneInjecting()
    } else {
      verify(registrar, times(expectedRanges.size))
          .startInjecting(ElLanguage)

      expectedRanges.forEach { textRange ->
        verify(registrar)
            .addPlace(null, null, context, textRange)
      }

      verify(registrar, times(expectedRanges.size))
          .doneInjecting()
    }
  }


}
