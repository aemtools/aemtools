package com.aemtools.index.indexer.osgi

import com.intellij.psi.PsiElement

interface OSGiPropertyMapper<T: PsiElement> {
  fun map(psiElement: T): Pair<String, String?>
}
