package com.aemtools.codeinsight.sling

import com.intellij.codeInsight.completion.JavaCompletionContributor.ANNOTATION_NAME
import com.intellij.codeInsight.completion.JavaPsiClassReferenceElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.ProximityLocation
import com.intellij.psi.util.proximity.ProximityWeigher

/**
 * @author Dmytro Troynikov
 */
class SlingModelAnnotationsCompletionWeighter : ProximityWeigher() {
  override fun weigh(element: PsiElement, location: ProximityLocation): Comparable<Nothing> {
    if (ANNOTATION_NAME.accepts(location.position)) {
      // todo: check that container class is annotated with @Model

      val fqn = (element as? JavaPsiClassReferenceElement)?.qualifiedName
          ?: return 0

      return when {
        fqn == "org.apache.sling.models.spi.injectorspecific.InjectAnnotation" ->
          -100
        fqn.startsWith("org.apache.sling.models.annotations") ->
          10
        fqn == "javax.inject.Inject" ->
          20
        else -> 0
      }
    }
    return 0
  }
}
