package com.aemtools.index.model

import com.aemtools.common.util.OpenApiUtil
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.getHtmlFile
import com.aemtools.common.util.normalizeToJcrRoot
import com.aemtools.common.util.toPsiFile
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.util.extractHtlHel
import com.aemtools.lang.util.htlVariableName
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import java.io.Serializable

/**
 * Container of template definition data. (`data-sly-template.name="${@ param1, param2}"
 *
 * @author Dmytro Primshyts
 */
data class TemplateDefinition(
    /**
     * Full name
     */
    var fullName: String?,
    /**
     * The name of the template
     */
    val name: String,
    /**
     * List of parameters declared in template.
     * e.g.
     *
     * ```
     * <div data-sly-template.template="${@ param1, param2}> -> [param1, param2]
     * ```
     *
     */
    val parameters: List<String>) : Serializable {

  /**
   * Get [XmlAttribute] in which current template was declared.
   *
   * @param project the project
   * @return declaration xml attribute
   */
  fun declarationElement(project: Project): XmlAttribute? {
    val file = OpenApiUtil.findFileByRelativePath(normalizedPath, project)
        ?.toPsiFile(project) as? com.aemtools.lang.htl.psi.HtlPsiFile
        ?: return null
    val htmlFile = file.getHtmlFile() ?: return null
    return htmlFile.findChildrenByType(XmlAttribute::class.java).find {
      it.htlVariableName() == name
    }
  }

  /**
   * Get parameter declaration element.
   *
   * @param project the project
   * @param parameter the parameter name
   * @return htl variable name
   */
  fun parameterDeclarationElement(project: Project, parameter: String): HtlVariableName? {
    val declarationElement = declarationElement(project)
        ?: return null

    val hel = declarationElement.extractHtlHel() as? com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin

    return hel?.getOptions()
        ?.find {
          it.name() == parameter
        }
        ?.contextExpression?.variableName
  }

  val containingDirectory: String
    get() {
      val _fullName = fullName
          ?: return ""
      return _fullName.substring(0, _fullName.lastIndexOf("/"))
    }

  /**
   * Return path starting from "/apps"
   */
  val normalizedPath: String
    get() {
      val _path = fullName
      return if (_path != null) {
        return _path.normalizeToJcrRoot()
      } else {
        ""
      }
    }

  /**
   * The name of html file
   */
  val fileName: String
    get() {
      val _path = fullName
      return _path?.substring(_path.lastIndexOf("/") + 1) ?: ""
    }

  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }

}
