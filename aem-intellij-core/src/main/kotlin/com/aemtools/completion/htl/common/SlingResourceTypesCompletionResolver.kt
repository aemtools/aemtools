package com.aemtools.completion.htl.common

import com.aemtools.index.model.AemComponentDefinition.Companion.toLookupElement
import com.aemtools.index.search.AemComponentSearch
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import org.apache.commons.lang3.StringUtils

object SlingResourceTypesCompletionResolver {

  private const val BASE_LINE: Double = 1.0
  private const val ONE_HUNDRED: Double = 100.0

  fun resolveDeclarations(project: Project, myNormalizedDirectory: String? = null) =
      AemComponentSearch.allComponentDeclarations(project)
          .filterNot {
            myNormalizedDirectory != null
                && (myNormalizedDirectory == it.resourceType() || myNormalizedDirectory.startsWith(it.resourceType()))
          }
          .map {
            val lookupElement = it.toLookupElement()

            if (myNormalizedDirectory == null) {
              lookupElement
            } else {
              PrioritizedLookupElement
                  .withPriority(lookupElement, calcPriority(lookupElement, myNormalizedDirectory))
            }
          }

  private fun calcPriority(lookupElement: LookupElement, myDirectory: String): Double {
    return BASE_LINE - StringUtils.getLevenshteinDistance(lookupElement.lookupString, myDirectory)
        .toDouble() / ONE_HUNDRED
  }
}
