package com.aemtools.index.model

import com.aemtools.common.util.jcrPropertyArray
import com.intellij.psi.xml.XmlTag
import java.io.Serializable

/**
 * @author Dmytro Primshyts
 */
data class ClientlibraryModel(
    val channels: List<String>,
    val categories: List<String>,
    val dependencies: List<String>,
    val embed: List<String>,
    val filePath: String
) : Serializable {

  companion object {

    /**
     * Create [ClientlibraryModel] from given [XmlTag].
     *
     * @param tag the tag
     * @param path full path to definition file
     *
     * @return client library model
     */
    fun fromTag(tag: XmlTag, path: String): ClientlibraryModel? {
      return ClientlibraryModel(
          channels = tag.jcrPropertyArray("channels"),
          categories = tag.jcrPropertyArray("categories"),
          dependencies = tag.jcrPropertyArray("dependencies"),
          embed = tag.jcrPropertyArray("embed"),
          filePath = path
      )
    }
  }

}
