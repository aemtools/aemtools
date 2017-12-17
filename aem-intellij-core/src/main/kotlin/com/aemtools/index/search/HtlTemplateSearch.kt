package com.aemtools.index.search

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.index.model.TemplateDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex

/**
 * Search methods for Htl templates.
 *
 * @author Dmytro Troynikov
 */
object HtlTemplateSearch {

  /**
   * Find all template definitions available in opened project.
   *
   * @param project the project
   * @return list of template definitions
   */
  fun all(project: Project): List<TemplateDefinition> {
    val fbi = FileBasedIndex.getInstance()
    val keys = fbi.getAllKeys(HtlTemplateIndex.HTL_TEMPLATE_ID, project)

    val values = keys.flatMap {
      fbi.getValues(HtlTemplateIndex.HTL_TEMPLATE_ID, it, GlobalSearchScope.projectScope(project))
    }.filterNotNull()

    return values
  }

  /**
   * Resolve use template by name, relative to given file.
   *
   * @param name the name of template
   * @param file the file where the template is used
   * @return list of available templates
   */
  fun resolveUseTemplate(name: String, file: PsiFile): List<TemplateDefinition> {
    val templates = all(file.project)
    return if (name.isAbsolutePath()) {
      templates.filter {
        it.normalizedPath == name
      }
    } else {
      val path = file.originalFile.containingDirectory.virtualFile.path
      val fileName = "$path/$name"
      templates.filter {
        it.fullName == fileName
      }
    }
  }

  private fun String.isAbsolutePath(): Boolean = this.startsWith("/")

}
