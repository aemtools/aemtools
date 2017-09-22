package com.aemtools.reference.htl.provider

import com.aemtools.completion.util.toPsiDirectory
import com.aemtools.index.search.AemComponentSearch
import com.aemtools.reference.common.reference.PsiDirectoryReference
import com.aemtools.util.OpenApiUtil
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import generated.psi.impl.HtlStringLiteralImpl

/**
 * @author Dmytro Troynikov
 */
object ResourceTypeReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(
      element: PsiElement,
      context: ProcessingContext): Array<PsiReference> {
    val literal = element as? HtlStringLiteralImpl
        ?: return emptyArray()

    val resourceType = literal.name
    val component = AemComponentSearch.findByResourceType(resourceType, literal.project)
        ?: return emptyArray()

    val componentFile = OpenApiUtil.findFileByRelativePath(component.fullPath, literal.project)
        ?: return emptyArray()

    val parentDir = componentFile.parent.toPsiDirectory(literal.project)
        ?: return emptyArray()

    return arrayOf(PsiDirectoryReference(
        parentDir,
        literal,
        TextRange(1, literal.textLength - 1)
    ))
  }

}
