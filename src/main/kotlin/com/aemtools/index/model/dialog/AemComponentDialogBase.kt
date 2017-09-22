package com.aemtools.index.model.dialog

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.completion.util.toPsiFile
import com.aemtools.util.OpenApiUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import java.io.Serializable

/**
 * Base component's dialog model.
 *
 * @property fullPath Full path to dialog file
 * @property resourceType The resource type of associated component
 * @property myParameters List of parameters
 *
 * @author Dmytro Troynikov
 */
abstract class AemComponentDialogBase<out PARAMETER : Serializable> : Serializable {

  abstract val fullPath: String
  abstract val resourceType: String
  abstract val myParameters: List<PARAMETER>

  /**
   * Find declaration element of parameter with given name.
   *
   * @param name name of parameter to look for
   * @param project the project
   *
   * @return psi element - declaration of property,
   * `null` if no declaration found for given property
   */
  fun declarationElement(name: String, project: Project): PsiElement? {
    val file = OpenApiUtil
        .findFileByRelativePath(
            fullPath.normalizeToJcrRoot(),
            project
        )
        ?.toPsiFile(project) as? XmlFile
        ?: return null

    val rootTag = file.rootTag
        ?: return null

    return rootTag.findChildrenByType(XmlTag::class.java)
        .find {
          it.attributes.any {
            it.value in listOf(name, "./$name")
          }
        }
  }

}
