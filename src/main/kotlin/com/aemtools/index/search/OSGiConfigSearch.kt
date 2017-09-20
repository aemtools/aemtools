package com.aemtools.index.search

import com.aemtools.index.OSGiConfigIndex
import com.aemtools.index.model.OSGiConfiguration
import com.aemtools.util.allScope
import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.util.indexing.FileBasedIndex

/**
 * Search of OSGi configuration files.
 *
 * @author Dmytro_Troynikov
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

    val filteredConfigs = configs.filter { it.fullQualifiedName == fqn }

    return if (!fillXmlFile) {
      filteredConfigs
    } else {
      val fileNames = filteredConfigs.map { it.fileName }
          .toSet()

      val files = fileNames.flatMap {
        FilenameIndex.getFilesByName(project, it, GlobalSearchScope.projectScope(project))
            ?.toList()
            .orEmpty()
      }

      filteredConfigs.forEach { config ->
        val matchedFile = files.find { file ->
          file.virtualFile.path == config.path
        } as? XmlFile
        config.xmlFile = matchedFile
      }
      filteredConfigs
    }
  }

  /**
   * Collect all OSGi configurations present in the project.
   *
   * @param project the project
   * @return list of all available OSGi configurations
   */
  fun getAllConfigs(project: Project): List<OSGiConfiguration> {
    val fbi = FileBasedIndex.getInstance()
    val keys = fbi.getAllKeys(OSGiConfigIndex.OSGI_INDEX_ID, project)

    val values = keys.flatMap {
      fbi.getValues(OSGiConfigIndex.OSGI_INDEX_ID, it, project.allScope())
    }.filterNotNull()

    return values
  }

}
