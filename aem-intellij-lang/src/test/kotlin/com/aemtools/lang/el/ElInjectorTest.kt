package com.aemtools.lang.el

import com.aemtools.test.util.mock
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author Dmytro Primshyts
 */
@RunWith(MockitoJUnitRunner::class)
class ElInjectorTest {

  val tested: ElInjector = ElInjector()

  @Mock
  lateinit var registrar: MultiHostRegistrar

  @Mock
  lateinit var context: PsiElement

  @Mock
  lateinit var containingFile: PsiFile

  @Before
  fun init() {
    context = mock<XmlAttributeValue>()
    `when`(containingFile.name)
        .thenReturn(".content.xml")

    `when`(context.containingFile)
        .thenReturn(containingFile)
  }

  @Test
  fun `elementsToInjectIn should return xml attribute value`() {
    assertThat(tested.elementsToInjectIn())
        .contains(XmlAttributeValue::class.java)
  }

  @Test
  fun `getLanguagesToInject should return on incorrect input type`() {
    context = mock<XmlTag>()

    tested.getLanguagesToInject(registrar, context)

    verify(registrar, never())
        .startInjecting(any())
  }

  @Test
  fun `getLanguagesToInject should return on incorrect file name`() {
    `when`(containingFile.name)
        .thenReturn("wrong.name")

    tested.getLanguagesToInject(registrar, context)

    verify(registrar, never())
        .startInjecting(any())
  }

}
