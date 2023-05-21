package com.aemtools.codeinsight.xml.schema

import com.aemtools.test.junit.MockitoExtension
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito

@ExtendWith(MockitoExtension::class)
class CqNamespaceImplicitUsageProviderTest {

  @Mock
  lateinit var xmlAttribute: XmlAttribute
  @Mock
  lateinit var psiFile: PsiFile
  @Mock
  lateinit var xmlTag: XmlTag
  @Mock
  lateinit var virtualFile: VirtualFile

  private val implicitUsageProvider = CqNamespaceImplicitUsageProvider()

  @Test
  fun shouldImplicitUsage() {
    Mockito.`when`(xmlAttribute.isNamespaceDeclaration).thenReturn(true)
    Mockito.`when`(xmlAttribute.localName).thenReturn("cq")
    Mockito.`when`(xmlAttribute.parent).thenReturn(xmlTag)
    Mockito.`when`(xmlTag.containingFile).thenReturn(psiFile)
    Mockito.`when`(psiFile.isValid).thenReturn(true)
    Mockito.`when`(psiFile.virtualFile).thenReturn(virtualFile)
    Mockito.`when`(virtualFile.path).thenReturn("/_cq_dialog.xml")

    assertTrue(implicitUsageProvider.isImplicitUsage(xmlAttribute))
  }

  @Test
  fun shouldNotImplicitUsageForNotCqNamespaceAttribute() {
    Mockito.`when`(xmlAttribute.isNamespaceDeclaration).thenReturn(true)
    Mockito.`when`(xmlAttribute.localName).thenReturn("jcr")
    Mockito.`when`(xmlAttribute.parent).thenReturn(xmlTag)
    Mockito.`when`(xmlTag.containingFile).thenReturn(psiFile)
    Mockito.`when`(psiFile.isValid).thenReturn(true)
    Mockito.`when`(psiFile.virtualFile).thenReturn(virtualFile)
    Mockito.`when`(virtualFile.path).thenReturn("_cq_dialog.xml")

    assertFalse(implicitUsageProvider.isImplicitUsage(xmlAttribute))
  }

  @Test
  fun shouldNotImplicitUsageForNotCqNamespaceFile() {
    Mockito.`when`(xmlAttribute.isNamespaceDeclaration).thenReturn(true)
    Mockito.`when`(xmlAttribute.parent).thenReturn(xmlTag)
    Mockito.`when`(xmlTag.containingFile).thenReturn(psiFile)
    Mockito.`when`(psiFile.isValid).thenReturn(true)
    Mockito.`when`(psiFile.virtualFile).thenReturn(virtualFile)
    Mockito.`when`(virtualFile.path).thenReturn(".content.xml")

    assertFalse(implicitUsageProvider.isImplicitUsage(xmlAttribute))
  }

  @Test
  fun shouldNotImplicitUsageForNotNameSpaceDeclaration() {
    Mockito.`when`(xmlAttribute.isNamespaceDeclaration).thenReturn(false)

    assertFalse(implicitUsageProvider.isImplicitUsage(xmlAttribute))
  }

  @Test
  fun shouldNotImplicitUsageForXmlTag() {
    assertFalse(implicitUsageProvider.isImplicitUsage(xmlTag))
  }

  @Test
  fun shouldNotImplicitRead() {
    assertFalse(implicitUsageProvider.isImplicitRead(xmlTag))
  }

  @Test
  fun shouldNotImplicitWrite() {
    assertFalse(implicitUsageProvider.isImplicitWrite(xmlTag))
  }
}
