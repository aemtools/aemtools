package com.aemtools.completion.reppolicy

import com.aemtools.completion.model.editconfig.XmlAttributeDefinition
import com.aemtools.completion.util.findParentByType
import com.aemtools.constant.const
import com.aemtools.service.ServiceFacade
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.impl.source.xml.XmlTagImpl
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov.
 */
class RepPolicyCompletionContributor : CompletionContributor {

  constructor() {
    extend(CompletionType.BASIC, PlatformPatterns.psiElement(), RepPolicyCompletionProvider())
  }

}

private class RepPolicyCompletionProvider : CompletionProvider<CompletionParameters>() {

  private val repPolicyRepository = ServiceFacade.getRepPolicyRepository()

  override fun addCompletions(
      parameters: CompletionParameters,
      context: ProcessingContext?,
      result: CompletionResultSet) {
    if (!accept(parameters)) {
      return
    }

    val currentElement = parameters.position as XmlToken
    val currentPositionType = extractCurrentPositionType(currentElement)

    val variantsGenerator = variantsProviders[currentPositionType] ?: return

    result.addAllElements(variantsGenerator(parameters, currentElement))
  }

  private val variantsForName: (CompletionParameters, XmlToken) -> List<LookupElement> = { _, token ->
    val tag = token.findParentByType(XmlTag::class.java)

    val tagDefinition = repPolicyRepository.getTagDefinitionByName((tag as XmlTagImpl).name)
    val tagAttributes = tag.attributes.map { it.name }
    val attributes = tagDefinition.attributes.filter { !tagAttributes.contains(it.name) }

    attributes.map {
      LookupElementBuilder.create(it.name).withInsertHandler(
          XmlAttributeInsertHandler()
      )
    }
  }

  private val variantsForValue: (CompletionParameters, XmlToken) -> List<LookupElement> = { _, token ->
    val tag = token.findParentByType(XmlTag::class.java)
    val tagDefinition = repPolicyRepository.getTagDefinitionByName((tag as XmlTagImpl).name)
    val attributeName = (token.findParentByType(XmlAttribute::class.java) as XmlAttribute).name
    val attributeDefinition: XmlAttributeDefinition? = tagDefinition.attributes.find { it.name == attributeName }

    attributeDefinition?.values.orEmpty().map { LookupElementBuilder.create(it) }
  }

  private val variantsForTagName: (CompletionParameters, XmlToken) -> List<LookupElement> = { _, token ->
    val parentTag = token.findParentByType(XmlTag::class.java)
        ?.findParentByType(XmlTag::class.java)

    val tagDefinition = repPolicyRepository.getTagDefinitionByName(
        (parentTag as XmlTagImpl).name)

    tagDefinition.childNodes.map {
      LookupElementBuilder.create(it)
    }
  }

  private fun extractCurrentPositionType(token: XmlToken): String {
    val currentPositionType = token.tokenType.toString()

    if (currentPositionType == const.xml.XML_ATTRIBUTE_NAME &&
        (token.findParentByType(XmlTag::class.java) as XmlTagImpl).name.contains(
            const.IDEA_STRING_CARET_PLACEHOLDER)) {
      return const.xml.XML_TAG_NAME
    }
    return currentPositionType
  }

  private fun accept(parameters: CompletionParameters): Boolean {
    return const.REP_POLICY == parameters.originalFile.name
  }

  private val variantsProviders = mapOf(
      const.xml.XML_ATTRIBUTE_NAME to variantsForName,
      const.xml.XML_ATTRIBUTE_VALUE to variantsForValue,
      const.xml.XML_TAG_NAME to variantsForTagName
  )

}
