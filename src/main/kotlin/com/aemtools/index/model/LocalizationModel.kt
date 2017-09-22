package com.aemtools.index.model

import com.aemtools.constant.const
import com.intellij.psi.xml.XmlTag
import java.io.Serializable

/**
 * Represents single entry from localization file.
 * Localization file is the '.content.xml' file with
 * `jcr:mixinTypes="mix:language"`.
 *
 * Valid entry should have following structure:
 *
 * ```
 * <tagname
 *      jcr:mixinTypes="[sling:Message]"
 *      jcr:primaryType="nt:folder"
 *      sling:key="Key Name"
 *      sling:message="Message Value"/>
 * ```
 *
 * The "sling:key" property is optional, in case if it is absent,
 * the name of tag will set the key name.
 *
 * @author Dmytro Troynikov
 */
data class LocalizationModel(
    /**
     * The name of containing file.
     */
    val fileName: String,
    /**
     * The language property (jcr:language) value.
     * I.E. the locale.
     */
    val language: String,
    /**
     * The message key.
     * Set by `sling:key` or by tag's name.
     */
    val key: String,
    /**
     * Value of `sling:message` property.
     * May contain "placeholders" e.g.:
     *
     * ```
     *  "Message with placeholder {0}"
     * ```
     * "{0}" is a placeholder that may be substituted
     * via `format` option.
     *
     * @see [placeholdersAmount]
     */
    val message: String
) : Serializable {

  /**
   * Collects amount of placeholders present
   * in current message.
   *
   * @return amount of placeholders
   */
  fun placeholdersAmount(): Int = message.let {
    "\\{\\d}".toRegex()
        .findAll(it)
        .count()
  }

  companion object {

    /**
     * Extract [LocalizationModel] object from given [XmlTag].
     *
     * @param tag the tag with localization data
     * @param fileName file name
     * @param language language
     *
     * @return new localization model, *null* in case if
     * given tag do not contain all required attributes
     */
    fun create(tag: XmlTag,
               fileName: String,
               language: String): LocalizationModel? {
      val key = tag.getAttributeValue(const.xml.SLING_KEY)
          ?: tag.name

      val message = tag.getAttributeValue(const.xml.SLING_MESSAGE)
          ?: return null

      return LocalizationModel(
          fileName,
          language,
          key,
          message
      )
    }
  }

}
