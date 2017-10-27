package com.aemtools.index.model

import com.aemtools.completion.util.normalizeToJcrRoot
import com.aemtools.constant.const.JCR_DESCRIPTION
import com.aemtools.constant.const.JCR_TITLE
import com.aemtools.constant.const.SLING_RESOURCE_SUPER_TYPE
import com.aemtools.constant.const.aem_component_declaration.COMPONENT_GROUP
import com.aemtools.constant.const.aem_component_declaration.CQ_ICON
import com.aemtools.constant.const.aem_component_declaration.IS_CONTAINER
import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.util.toStringBuilder
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlTag
import org.apache.commons.lang.BooleanUtils
import java.io.Serializable

/**
 * Represents content of AEM component descriptor
 * (`.content.xml`).
 *
 * @author Dmytro Troynikov
 */
data class AemComponentDefinition(

    /**
     * Represents the `jcr:title` attribute.
     */
    val title: String?,

    /**
     * Represents the `jcr:description` attribute.
     */
    val description: String?,

    /**
     * Full path of current component on file system.
     */
    val fullPath: String,

    /**
     * Represents the `sling:resourceSuperType` attribute.
     */
    val resourceSuperType: String?,

    /**
     * Represents the `componentGroup` attribute.
     */
    val componentGroup: String?,

    /**
     * Represents the `cq:isContainer` attribute.
     *
     * Default: *false*
     */
    val isContainer: Boolean = false,

    /**
     * Represents the `cq:icon` attribute.
     */
    val cqIcon: String? = null

) : Serializable {

  /**
   * Resource type of current component.
   *
   * @see [normalizeToJcrRoot]
   * @return resource type
   */
  fun resourceType(): String
      = fullPath.normalizeToJcrRoot().substringBeforeLast("/")

  /**
   * Get component name.
   *
   * `/apps/components/mycomponent/.content.xml -> mycomponent`
   *
   * @return component's name
   */
  fun componentName(): String
      = fullPath
      .substringBeforeLast("/")
      .substringAfterLast("/")

  companion object {

    @JvmStatic
    val serialVersionUID: Long = 1L

    /**
     * Extract [AemComponentDefinition] from given [XmlTag].
     *
     * Note: if component doesn't have `jcr:title` or `componentGroup` *null* will be returned.
     *
     * @param tag the tag
     * @param fullPath path to component
     * @return aem component definition
     */
    fun fromTag(tag: XmlTag, fullPath: String): AemComponentDefinition? {
      val title = tag.getAttributeValue(JCR_TITLE)
          ?: return null
      val group = tag.getAttributeValue(COMPONENT_GROUP)
          ?: return null
      return AemComponentDefinition(
          title = title,
          description = tag.getAttributeValue(JCR_DESCRIPTION),
          fullPath = fullPath,
          resourceSuperType = tag.getAttributeValue(SLING_RESOURCE_SUPER_TYPE),
          componentGroup = group,
          isContainer = BooleanUtils.toBoolean(
              tag.getAttributeValue(IS_CONTAINER)
          ),
          cqIcon = tag.getAttributeValue(CQ_ICON)
      )
    }

    /**
     * Convert current [AemComponentDefinition] to [LookupElement].
     *
     * @receiver [AemComponentDefinition]
     * @return lookup element
     */
    fun AemComponentDefinition.toLookupElement(): LookupElement =
        LookupElementBuilder.create(resourceType())
            .withTypeText("AEM Component")
            .withPresentableText(title ?: componentName())
            .withIcon(HtlIcons.AEM_COMPONENT)
            .let {
              if (title != null) {
                it.withTailText("(${componentName()})", true)
              } else {
                it
              }
            }

    /**
     * Create IDEA doc compliable string from current [AemComponentDefinition].
     *
     * @receiver [AemComponentDefinition]
     * @return documentation string
     */
    fun AemComponentDefinition.generateDoc(): String {
      val result = """AEM Component:<br/>""".toStringBuilder()
      with(result) {
        append("<b>Name</b>: ${componentName()}<br/>")
        append("<b>Group</b>: $componentGroup<br/>")
        title?.let { append("<b>jcr:title</b>: $it<br/>") }
        description?.let { append("<b>jcr:description</b>: $it<br/>") }
        resourceSuperType?.let { append("<b>sling:resourceSuperType</b>: $it<br/>") }

        append("<b>Container</b>: $isContainer<br/>")
        cqIcon?.let { append("<b>cq:icon</b>: $it") }
      }
      return result.toString()
    }

  }

}
