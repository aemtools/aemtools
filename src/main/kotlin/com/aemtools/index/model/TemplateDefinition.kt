package com.aemtools.index.model

import com.aemtools.completion.util.extractHtlHel
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.getHtmlFile
import com.aemtools.completion.util.htlVariableName
import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.completion.util.toPsiFile
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.aemtools.lang.htl.psi.HtlVariableName
import com.aemtools.lang.htl.psi.mixin.HtlElExpressionMixin
import com.aemtools.util.OpenApiUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import java.io.Serializable

/**
 * Container of template definition data. (`data-sly-template.name="${@ param1, param2}"
 *
 * @author Dmytro Troynikov
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
        ?.toPsiFile(project) as? HtlPsiFile
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
    val declarationElement = declarationElement(project) as? XmlAttribute
        ?: return null

    val hel = declarationElement.extractHtlHel() as? HtlElExpressionMixin

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
