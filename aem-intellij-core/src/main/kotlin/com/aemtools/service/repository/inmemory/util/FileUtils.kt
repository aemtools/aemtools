package com.aemtools.service.repository.inmemory.util

import com.google.gson.Gson
import com.intellij.openapi.util.io.FileUtil

/**
 * @author Dmytro Primshyts
 */
object FileUtils {

  /**
   * Read file content as string.
   *
   * @param fileName the file name
   * @return the content of file as string
   */
  fun readFileAsString(fileName: String): String {
    val input = FileUtils::class.java.classLoader
        .getResourceAsStream(fileName) ?: return ""

    return String(FileUtil.loadBytes(input))
  }

}

/**
 * Read list of Json objects from file with given name.
 *
 * @param fileName the file name
 * @return list of objects
 */
inline fun <reified T> readJson(fileName: String, gson: Gson = Gson()): List<T> {
  val jsonString = FileUtils.readFileAsString(fileName)
  return gson.fromJson(jsonString, emptyArray<T>().javaClass).toList()
}
