package com.aemtools.test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Dmytro Primshyts
 */
interface HtlTestCase {
  fun getTestDataPath(): String

  companion object {
    val testResourcesPath = "src/test/resources"
  }
}

fun HtlTestCase.pathToSourceTestFile(name: String, extension: String = "html"): Path =
    Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.$extension")

fun HtlTestCase.pathToGoldTestFile(name: String, extension: String = "txt"): Path =
    Paths.get("${HtlTestCase.testResourcesPath}/${getTestDataPath()}/$name.$extension")
