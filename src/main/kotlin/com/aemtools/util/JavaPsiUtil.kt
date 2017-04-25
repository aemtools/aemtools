package com.aemtools.util

import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.constant.const.java.SLING_SERVLET_ANNOTATION
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass

/**
 * @author Dmytro_Troynikov
 */

/**
 * Check if current [PsiClass] is an OSGi service.
 *
 * @return *true* if class is marked with corresponding
 * OSGi annotations, *false* otherwise
 */
fun PsiClass.isOSGiService(): Boolean {
    val annotations = this.modifierList?.children?.map {
        it as? PsiAnnotation
    }?.filterNotNull()
            ?: return false

    // TODO add check for OSGi declarative service
    return annotations.any {
        it.qualifiedName in listOf(FELIX_SERVICE_ANNOTATION,
                SLING_SERVLET_ANNOTATION,
                SLING_FILTER_ANNOTATION)
    }
}