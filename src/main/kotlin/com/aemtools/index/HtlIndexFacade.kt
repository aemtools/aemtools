package com.aemtools.index

import com.aemtools.completion.util.toPsiFile
import com.aemtools.index.model.LocalizationModel
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.util.allFromFbi
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PathUtil

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

    val normalizedName = normalizeName(name, psiFile)

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

    val normalizedName = normalizeName(name, psiFile)

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
        .mapNotNull { it.toPsiFile(relativeToFile.project) }
        .filterNot { it.name == relativeToFile.name }
  }

  /**
   * Collects all Htl files containing templates.
   *
   * @param project the project
   * @return list of [TemplateDefinition] objects
   */
  fun getTemplates(project: Project): List<TemplateDefinition> =
      allFromFbi(HtlTemplateIndex.HTL_TEMPLATE_ID, project)

  /**
   * Collect all localization models.
   *
   * @param project the project
   * @return list of [LocalizationModel] objects
   */
  fun getAllLocalizationModels(project: Project): List<LocalizationModel> =
      allFromFbi(LocalizationIndex.LOCALIZATION_INDEX, project)

  /**
   * Normalize file name relative to given psi file.
   * If the name contains absolute path (i.e. starts with '/')
   * it will returned as is.
   * If the name contains relative path
   * it will be appended to path of given psi file.
   *
   * @param name file name as it was entered in htl
   * @param psiFile the file in which the file inclusion is present
   * @return normalized name
   */
  private fun normalizeName(name: String, psiFile: PsiFile): String {
    return if (isAbsolutePath(name)) {
      name
    } else {
      with(psiFile.virtualFile.path) {
        substring(0, lastIndexOf('/')) + "/$name"
      }
    }
  }

  private fun isAbsolutePath(path: String): Boolean = path.startsWith("/")

}
