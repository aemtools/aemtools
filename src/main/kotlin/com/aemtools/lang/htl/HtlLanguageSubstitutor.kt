package com.aemtools.lang.htl

import com.aemtools.constant.const.JCR_ROOT_SEPARATED
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
        return if ((file.path.contains(JCR_ROOT_SEPARATED)
                && file.fileType === HtmlFileType.INSTANCE)
                // inject always in tests
                || iAmTest()) {
            HtlLanguage
        } else {
            null
        }

    }

}