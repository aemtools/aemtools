package com.aemtools.service.repository.inmemory.util

import org.apache.sanselan.util.IOUtils

/**
 * @author Dmytro_Troynikov
 */
object FileUtils {

    fun readFileAsString(fileName: String): String {
        val input = FileUtils::class.java.classLoader
            .getResourceAsStream(fileName)

        return String(IOUtils.getInputStreamBytes(input))
    }

}