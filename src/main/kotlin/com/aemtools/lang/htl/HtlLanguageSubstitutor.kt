package com.aemtools.lang.htl

import com.aemtools.constant.const.JCR_ROOT_SEPARATED
import com.aemtools.settings.HtlRootDirectories
import com.aemtools.util.OpenApiUtil.iAmTest
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor

/**
 * @author Dmytro Troynikov
 */
class HtlLanguageSubstitutor : LanguageSubstitutor() {

    override fun getLanguage(file: VirtualFile, project: Project): Language? {
        return if ((htlPath(file.path)
                && file.fileType === HtmlFileType.INSTANCE)
                // inject always in tests
                || iAmTest()) {
            HtlLanguage
        } else {
            null
        }

    }

    /**
     * Check if given path is correct Htl path
     * should either contain `jcr_root` or be in
     * htl roots list.
     *
     * @param path the path
     *
     * @return *true* if path is correct htl path, *false* otherwise
     */
    private fun htlPath(path: String): Boolean {
        return path.contains(JCR_ROOT_SEPARATED)
                || HtlRootDirectories.getInstance()
                ?.let { htlRootDirectories ->
                    htlRootDirectories.directories
                            .any {
                                path.startsWith(it)
                            }
                } ?: false
    }

}