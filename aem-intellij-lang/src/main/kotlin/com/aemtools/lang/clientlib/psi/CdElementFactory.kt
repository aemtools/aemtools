package com.aemtools.lang.clientlib.psi

import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.psiFileFactory
import com.aemtools.lang.clientlib.file.CdFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory

object CdElementFactory {

  fun createCdInclude(value: String, project: Project): CdInclude? =
      project.psiFileFactory()
          .file(value)
          .findChildrenByType(CdInclude::class.java)
          .firstOrNull()

  private fun PsiFileFactory.file(text: String): PsiFile =
      createFileFromText("js.txt", CdFileType, text)
}
