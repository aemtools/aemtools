package com.aemtools.reference.htl.provider

import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.aemtools.lang.java.JavaSearch
import com.aemtools.util.allScope
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext

/**
 * Reference provider for Htl string literals.
 *
 * @author Dmytro Troynikov
 */
object DataSlyUseElJavaReferenceProvider : JavaClassReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
    val literal = element as? HtlStringLiteralMixin ?: return emptyArray()

    val psiClass = JavaSearch.findClass(literal.name, literal.project) ?: return emptyArray()

    return getReferencesByString(psiClass.qualifiedName, literal, 1)
  }

  /**
   * Intentionally using global scope here,
   * since Model classes can be defined in another module, which is not explicitly
   * declared as a dependency for current module, resulting in non-resolvable reference.
   * @see com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference.getScope
   */
  override fun getScope(project: Project?): GlobalSearchScope? {
    return project?.allScope()
  }
}
