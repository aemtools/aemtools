package com.aemtools.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

/**
 * @author Dmytro_Troynikov
 */
abstract class BaseDocumentationProvider : AbstractDocumentationProvider() {

    /**
     * Check if current documentation provider may process `generateDoc` action
     * on given [PsiElement].
     * @param element element to check against
     * @return __true__ if current documentation provider can generate documentation
     * for given element
     */
    abstract fun acceptGenerateDoc(element: PsiElement) : Boolean

}