package com.aemtools.index.search

import com.aemtools.common.util.allScope
import com.aemtools.common.util.toPsiFile
import com.aemtools.index.OSGiConfigIndex
import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.index.model.OSGiConfigurationIndexModel
import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.util.indexing.FileBasedIndex

/**
 * Search of OSGi configuration files.
 *
 * @author Dmytro Primshyts
 */
object OSGiConfigSearch {

  /**
   * Collect all available OSGi configurations for given class.
   *
   * @param fqn the full qualified name of class to look for
   * @param project the project
   * @param fillXmlFile (_default false_) defines if the [OSGiConfiguration.xmlFile] should be filled
   *
   * @return list of all available OSGi configurations,
   * empty list will be returned if no OSGi configuration was found
   */
  fun findConfigsForClass(fqn: String, project: Project, fillXmlFile: Boolean = false): List<OSGiConfiguration> {
    val configs = getAllConfigs(project)

    val mapped = configs
        .map { OSGiConfiguration(it.path, it.parameters) }
        .filter { it.fullQualifiedName == fqn }
    return if (!fillXmlFile) {
      mapped
    } else {
      val fileNames = mapped.map { it.fileName }
          .toSet()

      val virtualFiles = fileNames.flatMap {
        FilenameIndex.getVirtualFilesByName(it, GlobalSearchScope.projectScope(project))
      }

      mapped.forEach { config ->
        val matchedFile = virtualFiles.find { virtualFile ->
          virtualFile.path == config.path
        }?.toPsiFile(project) as? XmlFile
        config.xmlFile = matchedFile
      }
      mapped
    }
  }

  /**
   * Collect all OSGi configurations present in the project.
   *
   * @param project the project
   * @return list of all available OSGi configurations
   */
  private fun getAllConfigs(project: Project): List<OSGiConfigurationIndexModel> {
    val fbi = FileBasedIndex.getInstance()
    val keys = fbi.getAllKeys(OSGiConfigIndex.OSGI_INDEX_ID, project)

    val values = keys.flatMap {
      fbi.getValues(OSGiConfigIndex.OSGI_INDEX_ID, it, project.allScope())
    }.filterNotNull()

    return values
  }

}
