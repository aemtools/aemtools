package com.aemtools.service.detection

import com.aemtools.constant.const.JCR_ROOT
import com.aemtools.constant.const.JCR_ROOT_SEPARATED
import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.OpenApiUtil.iAmTest

/**
 * The service
 *
 * @author Dmytro Troynikov
 */
object HtlDetectionService {

    @field:Volatile
    var markAllInTest: Boolean = true

    /**
     * Check if given path is a correct htl root path.
     *
     * @return *true* if given path may be marked as Htl root, *false* otherwise
     */
    fun eligibleHtlRoot(path: String): Boolean {
        val htlRootDirectories = HtlRootDirectories.getInstance()
                ?: return false

        return htlRootDirectories.directories.any { dir ->
            dir.startsWith(path)
        } or !path.contains(JCR_ROOT_SEPARATED)
    }

    /**
     * Check if given file is Htl file.
     *
     * @param fileName the file name
     * @return *true* if given file name is recognized as Htl,
     * *false* otherwise
     */
    fun isHtlFile(fileName: String): Boolean {
        val extension = fileName.substringAfterLast(".")
        if (extension != "html") {
            return false
        }

        HtlRootDirectories.getInstance()?.let { roots ->
            if (roots.directories.any { fileName.startsWith(it) }) {
                return true
            }
        }

        return (markAllInTest && iAmTest()) || fileName.contains(JCR_ROOT_SEPARATED)
    }

    /**
     * Check if given directory is one of Htl roots.
     *
     * @param path directory to check
     * @return *true* if given directory is Htl root, *false* otherwise
     */
    fun isHtlRootDirectory(path: String): Boolean =
            HtlRootDirectories.getInstance()
                    ?.directories?.contains(path) ?: false
                    || path.substringAfterLast("/") == JCR_ROOT

    /**
     * Check if given path lays under some of Htl roots.
     *
     * @param path the path to check
     * @return *true* if given path is part of Htl root,
     * *false* otherwise
     */
    fun isUnderHtlRoot(path: String): Boolean {
        HtlRootDirectories.getInstance()?.let { roots ->
            if (roots.directories.any { path.startsWith(it) }) {
                return true
            }
        }

        return path.contains(JCR_ROOT_SEPARATED)
    }

}
