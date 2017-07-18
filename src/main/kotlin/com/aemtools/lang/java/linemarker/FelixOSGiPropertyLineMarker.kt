package com.aemtools.lang.java.linemarker

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.index.model.sortByMods
import com.aemtools.index.search.OSGiConfigSearch
import com.aemtools.lang.java.linemarker.markerinfo.FelixOSGiPropertyDescriptor
import com.aemtools.lang.java.linemarker.markerinfo.FelixOSGiPropertyMarkerInfo
import com.aemtools.util.isFelixProperty
import com.aemtools.util.isOSGiService
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.xml.XmlAttribute

/**
 * @author Dmytro Troynikov
 */
class FelixOSGiPropertyLineMarker : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        val identifier = element as? PsiIdentifier
                ?: return null
        val containingClass = identifier.findParentByType(PsiClass::class.java)
                ?: return null
        val field = identifier.parent as? PsiField
                ?: return null

        if (containingClass.isOSGiService()
                && field.isFelixProperty()) {

            val value = field.computeConstantValue()

            val containingClassFqn = containingClass.qualifiedName ?: return null

            val configs = OSGiConfigSearch.findConfigsForClass(
                    containingClassFqn,
                    element.project,
                    true)
            if (configs.isEmpty()) {
                return null
            }

            val propertyDescriptors = configs
                    .sortByMods()
                    .map { config ->
                        val file = config.xmlFile
                        val attribute = file.findChildrenByType(XmlAttribute::class.java)
                                .find { it.name == value }
                                ?: return@map null

                        val attributeValue = attribute.value ?: return@map null

                        FelixOSGiPropertyDescriptor(
                                config.mods.joinToString { it },
                                attributeValue,
                                attribute
                        )
                    }.filterNotNull()
                    .let { propertyDescriptors ->
                        padModsByMaxModLength(propertyDescriptors)
                    }

            return FelixOSGiPropertyMarkerInfo(element,
                    propertyDescriptors)
        }

        return null
    }


    override fun collectSlowLineMarkers(elements: MutableList<PsiElement>, result: MutableCollection<LineMarkerInfo<PsiElement>>) {
    }

    private fun padModsByMaxModLength(propertyDescriptors: List<FelixOSGiPropertyDescriptor>): List<FelixOSGiPropertyDescriptor> {
        val modsMaxLength = propertyDescriptors
                .maxBy {
                    it.mods.length
                }?.mods?.length
                ?: 0
        return propertyDescriptors.map {
            it.copy(
                    mods = it.mods.padEnd(
                            modsMaxLength
                    )
            )
        }
    }

}