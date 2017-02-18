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

    fun resolveFile(name: String, psiFile: PsiFile): PsiFile? {

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

        val normalizedName = if (isAbsolutePath(name)) {
            name
        } else {
            with (psiFile.virtualFile.path) {
                substring(0, lastIndexOf('/')) + "/$name"
            }
        }

        val files = FilenameIndex
                .getAllFilesByExt(psiFile.project, extension, GlobalSearchScope.projectScope(psiFile.project))
        val file = files.find { it.path.endsWith(normalizedName) }
                ?: return null
        return PsiManager.getInstance(psiFile.project).findFile(file)
    }

    /**
     * Collects all Htl files containing templates.
     * @return list of [TemplateDefinition] objects
     */
    fun getTemplates(project: Project): List<TemplateDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, project)
        val result = keys.flatMap {
            fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, GlobalSearchScope.allScope(project))
        }
        return result
    }

    private fun isAbsolutePath(path: String) : Boolean = path.startsWith("/")

}