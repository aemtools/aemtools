package com.aemtools.lang.htl.file

import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

/**
 * @author Dmytro Troynikov
 */
class HtlViewProviderFactory : FileViewProviderFactory {

    override fun createFileViewProvider(file: VirtualFile,
                                        language: Language?,
                                        manager: PsiManager,
                                        eventSystemEnabled: Boolean): FileViewProvider {
        if (language == null) {
            throw AssertionError()
        }

        return HtlFileViewProvider(manager, file, eventSystemEnabled, language)
    }

}