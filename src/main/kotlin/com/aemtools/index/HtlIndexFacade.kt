package com.aemtools.index

import com.aemtools.completion.util.toPsiFile
import com.aemtools.index.model.LocalizationModel
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.util.allScope
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PathUtil
import com.intellij.util.indexing.FileBasedIndex

/**
 * @author Dmytro Troynikov
 */
object HtlIndexFacade {

    private val SLY_USE_EXTENSIONS = listOf("js", "html", "jsp")
    private val SLY_INCLUDE_EXTENSIONS = listOf("html", "jsp")

    /**
     * Resolve file for *data-sly-use*.
     *
     * @param name the name of file
     * @param psiFile the "relative" file
     * @return resolved file, _null_ if no file was found
     */
    fun resolveUseFile(name: String, psiFile: PsiFile): PsiFile? {
        val extension = PathUtil.getFileExtension(name)
                ?: return null
        if (extension !in SLY_USE_EXTENSIONS) {
            return null
        }

        val normalizedName = if (isAbsolutePath(name)) {
            name
        } else {
            with(psiFile.virtualFile.path) {
                substring(0, lastIndexOf('/')) + "/$name"
            }
        }

        val files = FilenameIndex
                .getAllFilesByExt(psiFile.project,
                        extension,
                        GlobalSearchScope.projectScope(psiFile.project))
        val file = files.find { it.path.endsWith(normalizedName) }
                ?: return null

        return file.toPsiFile(psiFile.project)
    }

    /**
     * Resolve file for *data-sly-include*.
     *
     * @param name the name of file
     * @param psiFile the "relative" file
     * @return resolved file, _null_ if no file was found
     */
    fun resolveIncludeFile(name: String, psiFile: PsiFile): PsiFile? {
        val extension = PathUtil.getFileExtension(name)
                ?: return null

        if (extension !in SLY_INCLUDE_EXTENSIONS) {
            return null
        }

        val normalizedName = if (isAbsolutePath(name)) {
            name
        } else {
            with(psiFile.virtualFile.path) {
                substring(0, lastIndexOf('/')) + "/$name"
            }
        }

        val files = FilenameIndex
                .getAllFilesByExt(psiFile.project,
                        extension,
                        GlobalSearchScope.projectScope(psiFile.project))
        val file = files.find { it.path.endsWith(normalizedName) }
                ?: return null

        return file.toPsiFile(psiFile.project)
    }

    /**
     * Find files which may be included into given file via *data-sly-include*
     * (`html` and `jsp` files are allowed).
     *
     * @param relativeToFile the file to look includables against
     * @return list of files which may be included into given file
     */
    fun includableFiles(relativeToFile: PsiFile): List<PsiFile> {
        val files = SLY_INCLUDE_EXTENSIONS.flatMap { extension ->
            FilenameIndex
                    .getAllFilesByExt(relativeToFile.project, extension, GlobalSearchScope.projectScope(relativeToFile.project))
        }

        val relativeToDir = relativeToFile.containingDirectory.virtualFile.path

        return files.filter { it.path.startsWith(relativeToDir) }
                .map { it.toPsiFile(relativeToFile.project) }
                .filterNotNull()
                .filterNot { it.name == relativeToFile.name }
    }

    /**
     * Collects all Htl files containing templates.
     *
     * @param project the project
     * @return list of [TemplateDefinition] objects
     */
    fun getTemplates(project: Project): List<TemplateDefinition> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, project)
        val result = keys.flatMap {
            fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, project.allScope())
        }
        return result
    }

    /**
     * Collect all localization models.
     *
     * @param project the project
     * @return list of [LocalizationModel] objects
     */
    fun getAllLocalizationModels(project: Project): List<LocalizationModel> {
        val fbi = FileBasedIndex.getInstance()
        val keys = fbi.getAllKeys(LocalizationIndex.LOCALIZATION_INDEX, project)

        val result = keys.flatMap {
            fbi.getValues(LocalizationIndex.LOCALIZATION_INDEX, it, project.allScope())
        }

        return result
    }

    private fun isAbsolutePath(path: String): Boolean = path.startsWith("/")

}