package com.aemtools.common.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

/**
 * @author Dmytro Primshyts
 */
open class BaseCompletionContributor(
    contributorDsl: CompletionContributorDsl.() -> Unit
) : CompletionContributor() {
  init {
    val result = CompletionContributorDsl()
    contributorDsl.invoke(result)
    result.extensions.forEach { extension ->
      extend(
          extension.type,
          extension.pattern,
          extension.provider
      )
    }
  }
}

class CompletionContributorDsl {

  internal data class ExtensionModel(
      val type: CompletionType,
      val pattern: ElementPattern<out PsiElement>,
      val provider: CompletionProvider<in CompletionParameters>
  )

  internal val extensions = mutableListOf<ExtensionModel>()

  fun extend(
      type: CompletionType,
      pattern: ElementPattern<out PsiElement>,
      provider: CompletionProvider<in CompletionParameters>
  ) {
    extensions += ExtensionModel(
        type,
        pattern,
        provider
    )
  }

  fun basic(
      pattern: ElementPattern<out PsiElement>,
      provider: CompletionProvider<in CompletionParameters>
  ) = extend(CompletionType.BASIC, pattern, provider)

  fun smart(
      pattern: ElementPattern<out PsiElement>,
      provider: CompletionProvider<in CompletionParameters>
  ) = extend(CompletionType.BASIC, pattern, provider)

}
