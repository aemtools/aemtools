package com.aemtools.completion.htl.provider

import com.aemtools.completion.htl.common.FileVariablesResolver
import com.aemtools.completion.htl.model.declaration.HtlUseVariableDeclaration
import com.aemtools.lang.java.JavaUtilities
import com.aemtools.util.elMembers
import com.aemtools.util.resolveReturnType
import com.aemtools.util.toPsiClass
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMember
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlListSmartCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(parameters: CompletionParameters,
                              context: ProcessingContext?,
                              result: CompletionResultSet) {
    val currentPosition = parameters.position

    val useObjects = FileVariablesResolver.declarationsForPosition(currentPosition)
        .mapNotNull { it as? HtlUseVariableDeclaration }

    useObjects.mapNotNull { useObject ->
      val myName = useObject.variableName
      val myClass = useObject.useClass() ?: return@mapNotNull null

      val lookupElements = myClass.elMembers()
          .mapNotNull {
            val psiClass = it.resolveReturnType()?.toPsiClass()
            val memberName = it.name
            if (psiClass != null && memberName != null) {
              memberName to psiClass
            } else {
              null
            }
          }.let { pairs ->
        pairs.flatMap { extractNested(myName, it.first, it.second) }
      }
      lookupElements
    }.flatten()
        .let { lookupElements ->
          result.addAllElements(lookupElements)
          result.stopHere()
        }

  }

  private fun extractNested(prefix: String,
                            name: String,
                            psiClass: PsiClass): List<LookupElement> {
    if (psiClass.isIterable()) {
      return listOf(
          LookupElementBuilder.create("$prefix.$name")
              .withTailText(psiClass.qualifiedName)
      )
    }

    return psiClass.elMembers()
        .filterNot {
          it.containingClass?.qualifiedName == "java.lang.Object"
        }
        .mapNotNull {
          val psiClass = it.resolveReturnType()?.toPsiClass()
          val memberName = it.name
          if (psiClass != null && memberName != null) {
            memberName to psiClass
          } else {
            null
          }
        }.let {
      it.flatMap { nested ->
        extractNested("$prefix.$name", nested.first, nested.second)
      }
    }
  }

}

private fun PsiMember.isIterable(): Boolean {
  val returnType = this.resolveReturnType()
      ?.toPsiClass() ?: return false
  return JavaUtilities.isIterable(returnType)
      || JavaUtilities.isIterable(returnType)
}
