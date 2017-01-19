package com.aemtools.completion.html

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isSlyTag
import com.aemtools.completion.util.isUniqueHtlAttribute
import com.aemtools.constant.const
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.XmlAttributeInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import java.util.*

/**
 * Provider of Htl specific attributes.
 * @author Dmytro_Troynikov
 */
object HtmlAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters,
                                context: ProcessingContext?,
                                result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val tag = parameters.position.findParentByType(XmlTag::class.java)

        var resultVariants = vars;
        if (tag != null) {
            resultVariants = filterLookupElementsForTag(tag, vars)
        }
        result.addAllElements(resultVariants)
    }

    /**
     * Filter out obsolete attribute for autocompletion
     */
    private fun filterLookupElementsForTag(tag: XmlTag, vars: List<LookupElement>): List<LookupElement> {
        val obsoleteAttributes = ArrayList<String>()
        if (tag.isSlyTag()) {
            obsoleteAttributes.add(DATA_SLY_UNWRAP)
        }

        obsoleteAttributes.addAll(getUniqueHtlAttributes(tag))
        return vars.filter { !obsoleteAttributes.contains(it.lookupString) }
    }

    /**
     * Method return attributes which have to be unique attributes in tag and already exist
     * in tag.
     *
     * @param tag XmlTag where method search unique attributes
     * @return collection of names of attributes
     */
    private fun getUniqueHtlAttributes(tag: XmlTag?): Collection<String> =
            tag.findChildrenByType(XmlAttribute::class.java)
                    .filter(XmlAttribute::isUniqueHtlAttribute)
                    .map { it.name }

    private val vars: List<LookupElement> = const.htl.HTL_ATTRIBUTES.map {
        val result = LookupElementBuilder.create(it)
                .withTypeText("HTL Attribute")
        if (it == DATA_SLY_UNWRAP) {
            result
        } else {
            result.withInsertHandler(XmlAttributeInsertHandler())
        }
    }

}
