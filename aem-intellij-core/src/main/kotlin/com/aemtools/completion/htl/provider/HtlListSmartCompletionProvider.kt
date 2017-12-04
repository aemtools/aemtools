package com.aemtools.completion.htl.provider

import com.aemtools.analysis.htl.callchain.elements.virtual.BaseVirtualCallChainElement
import com.aemtools.analysis.htl.callchain.elements.virtual.VirtualCallChainElement
import com.aemtools.analysis.htl.callchain.typedescriptor.java.JavaPsiClassTypeDescriptor
import com.aemtools.analysis.htl.resolution.VirtualChainResolver
import com.aemtools.codeinsight.htl.model.HtlUseVariableDeclaration
import com.aemtools.completion.htl.common.FileVariablesResolver
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
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
      val virtualCallChainElement = useObject.toVirtualCallChainElement() ?: return@mapNotNull null

      VirtualChainResolver.nestedIterables(virtualCallChainElement)
    }.flatten()
        .let { lookupElements ->
          result.addAllElements(lookupElements)
          result.stopHere()
        }

  }

}

private fun HtlUseVariableDeclaration.toVirtualCallChainElement(): VirtualCallChainElement? {
  val psiClass = useClass() ?: return null
  return BaseVirtualCallChainElement(
      variableName,
      JavaPsiClassTypeDescriptor.create(psiClass)
  )
}
