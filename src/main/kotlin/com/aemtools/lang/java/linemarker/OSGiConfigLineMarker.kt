package com.aemtools.lang.java.linemarker

import com.aemtools.index.search.OSGiConfigSearch
import com.aemtools.util.isOSGiService
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author Dmytro_Troynikov
 */
class OSGiConfigLineMarker : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (element is PsiIdentifier && element.parent is PsiClass) {
            val psiClass = element.parent as? PsiClass
                    ?: return null

            if (psiClass.isOSGiService()) {
                val fqn = psiClass.qualifiedName ?: return null
                val configs = OSGiConfigSearch.findConfigsForClass(fqn, element.project)
                if (configs.isEmpty()) {
                    return null
                }

                val files = FilenameIndex.getFilesByName(element.project,
                        configs.first().fileName,
                        GlobalSearchScope.allScope(element.project))
                        .toSet()
                        .filterNotNull()

                return OSGiServiceConfigMarkerInfo(element, configs, files)
            }
        }
        return null
    }

    override fun collectSlowLineMarkers(elements: MutableList<PsiElement>, result: MutableCollection<LineMarkerInfo<PsiElement>>) {
        val classIdentifier = elements.map { it as? PsiIdentifier }
                .filterNotNull()
                .find {
                    it.parent is PsiClass
                } ?: return
        val marker = getLineMarkerInfo(classIdentifier)
                ?: return
        result.add(marker)
    }
}