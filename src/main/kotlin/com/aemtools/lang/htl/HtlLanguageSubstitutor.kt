package com.aemtools.lang.htl

import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor

/**
 * @author Dmytro Troynikov
 */
class HtlLanguageSubstitutor : LanguageSubstitutor() {

    override fun getLanguage(file: VirtualFile, project: Project): Language? {
        return HtlLanguage
    }

}