package com.aemtools.lang.htl.psi

import com.aemtools.lang.htl.HtlLanguage
import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.impl.PsiFileEx
import javax.swing.Icon

/**
* @author Dmytro Troynikov
*/
class HtlPsiFile(fileViewProvider: FileViewProvider)
    : PsiFileBase(fileViewProvider, HtlLanguage), PsiFileEx {

    override fun getFileType(): FileType
        = HtlFileType

    override fun toString() = "HtlFile:$name"

}