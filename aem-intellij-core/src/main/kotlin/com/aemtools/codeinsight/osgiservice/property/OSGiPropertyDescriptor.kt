package com.aemtools.codeinsight.osgiservice.property

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/**
 * Descriptor of single OSGi property.
 *
 * @author Dmytro Primshyts
 */
data class OSGiPropertyDescriptor(
    /**
     * Mods string.
     */
    val mods: String,
    /**
     * Value of property.
     */
    val propertyValue: String,
    /**
     * PSI element that contains declaration of property.
     */
    val containingPsiElement: PsiElement?,

    /**
     * OSGi configuration containing file.
     */
    val containingPsiFile: PsiFile
)
