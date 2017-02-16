package com.aemtools.index

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro Troynikov
 */
object HtlIndexFacade {

    fun resolveSlyFile(name: String, psiFile: PsiFile): PsiFile? {

        val extension = with(name) {
            if (contains(".") && length > lastIndexOf(".")) {
                substring(lastIndexOf(".") + 1)
            } else {
                return null
            }
        }

        if (extension !in listOf("js", "html")) {
            return null
        }

        val currentDirectory = psiFile.containingDirectory

        val files = FilenameIndex
                .getAllFilesByExt(psiFile.project, extension, GlobalSearchScope.projectScope(psiFile.project))
        val file = files.find { it.name == name }
                ?: return null
        return PsiManager.getInstance(psiFile.project).findFile(file)
    }

    fun getTemplates(project: Project): List<TemplateDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, project)
        val result = keys.flatMap {
            fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, GlobalSearchScope.allScope(project))
        }
        return result
    }


}