package com.aemtools.lang.java.linemarker

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator
import com.intellij.ide.util.PsiElementListCellRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import java.awt.event.MouseEvent

/**
 * @author Dmytro_Troynikov
 */
class OSGiGutterIconNavigationHandler(
        val myReferences: List<PsiFile>,
        val myTitle: String
) : GutterIconNavigationHandler<PsiElement> {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    private val messages: Map<String, String> = myReferences.map {
        it.virtualFile.path to extractModes(it)
    }.toMap()

    private fun extractModes(file: PsiFile): String {
        val path = file.containingDirectory.virtualFile.path
        val configPart = path.substring(path.lastIndexOf("config"))
        val mods = configPart.split(".")
        return if (mods.size == 1) {
            "default"
        } else {
            mods.subList(1, mods.size)
                    .joinToString { it }
        }
    }

    override fun navigate(e: MouseEvent?, elt: PsiElement?) {
        PsiElementListNavigator.openTargets(e,
                myReferences.toTypedArray(),
                myTitle, null, createListCellRenderer())
    }

    private fun createListCellRenderer(): PsiElementListCellRenderer<PsiFile> {
        return object : PsiElementListCellRenderer<PsiFile>() {
            override fun getIconFlags(): Int {
                return 0
            }

            override fun getElementText(element: PsiFile?): String {
                val path = element?.virtualFile?.path
                        ?: return "Unknown"
                return messages[path]
                        ?: return "Unknown"
            }

            override fun getContainerText(element: PsiFile?, name: String?): String? {
                return ""
            }

        }
    }

}