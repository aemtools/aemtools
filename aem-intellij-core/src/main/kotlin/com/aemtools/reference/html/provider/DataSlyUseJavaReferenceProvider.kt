package com.aemtools.reference.html.provider

import com.aemtools.common.util.allScope
import com.aemtools.common.util.findParentByType
import com.aemtools.lang.java.JavaSearch
import com.aemtools.lang.util.isDataSlyUse
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.ProcessingContext

/**
 * Adds references in data-sly-use attributes.
 *
 * @author Dmytro Primshyts
 */
object DataSlyUseJavaReferenceProvider : JavaClassReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
    val attr = element.findParentByType(XmlAttribute::class.java) ?: return arrayOf()
    val value = attr.valueElement?.value ?: return arrayOf()
    if (attr.isDataSlyUse()) {
      val psiClass = JavaSearch.findClass(value, element.project)
          ?: return arrayOf()

      return getReferencesByString(psiClass.qualifiedName, element, 1)
    }
    return arrayOf()
  }

  /**
   * Intentionally using global scope here,
   * since Model classes can be defined in another module, which is not explicitly
   * declared as a dependency for current module, resulting in non-resolvable reference.
   *
   * @see com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference.getScope
   */
  override fun getScope(project: Project): GlobalSearchScope {
    return project.allScope()
  }
}
