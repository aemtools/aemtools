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

        val virtualFile = OpenApiUtil.findFileByPath(component.fullPath)
                ?: return emptyArray()

        val parentDir = virtualFile.parent.toPsiDirectory(literal.project)
                ?: return emptyArray()

        // todo double check if reference to directory is more convenient than reference to .content.xml file
        return arrayOf(PsiDirectoryReference(
                parentDir,
                literal,
                TextRange(1, literal.textLength - 1)
        ))
    }

}