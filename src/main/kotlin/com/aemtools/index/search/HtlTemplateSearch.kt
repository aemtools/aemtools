package com.aemtools.index.search

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.index.model.TemplateDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro Troynikov
 */
object HtlTemplateSearch {

    fun all(project: Project): List<TemplateDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, project)

        val values = keys.flatMap {
            fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, GlobalSearchScope.projectScope(project))
        }.filterNotNull()

        return values
    }

    fun resolveUseTemplate(name: String, file: PsiFile): List<TemplateDefinition> {
        val templates = all(file.project)
        return if (name.isAbsolutePath()) {
            templates.filter {
                it.normalizedPath == name
            }
        } else {
            val fileDir = file.originalFile.containingDirectory.virtualFile.path
            templates.filter {
                it.containingDirectory.startsWith(fileDir)
            }
        }
    }

    private fun String.isAbsolutePath(): Boolean = this.startsWith("/")

}