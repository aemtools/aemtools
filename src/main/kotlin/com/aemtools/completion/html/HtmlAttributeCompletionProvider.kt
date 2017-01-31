package com.aemtools.completion.html

import com.aemtools.completion.htl.inserthandler.HtlExpressionInsertHandler
import com.aemtools.completion.htl.inserthandler.HtlIdentifierInsertHandler
import com.aemtools.completion.util.findChildrenByType
import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isSlyTag
import com.aemtools.completion.util.isUniqueHtlAttribute
import com.aemtools.constant.const
import com.aemtools.constant.const.htl.DATA_SLY_ATTRIBUTE
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.constant.const.htl.DATA_SLY_ELEMENT
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_RESOURCE
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_TEXT
import com.aemtools.constant.const.htl.DATA_SLY_UNWRAP
import com.aemtools.constant.const.htl.DATA_SLY_USE
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

    // Attributes, which need expression after defining
    // (For example: data-sly-include=${})
    val HTL_ATTRIBUTES_WITH_EXPRESSION = listOf(
        DATA_SLY_TEST,
        DATA_SLY_LIST,
        DATA_SLY_REPEAT,
        DATA_SLY_TEXT,
        DATA_SLY_ELEMENT,
        DATA_SLY_CALL,
        DATA_SLY_INCLUDE,
        DATA_SLY_RESOURCE,
        DATA_SLY_ATTRIBUTE
    )

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
        when (it) {
            in HTL_ATTRIBUTES_WITH_EXPRESSION -> result.withInsertHandler(HtlExpressionInsertHandler())

            DATA_SLY_USE, DATA_SLY_TEMPLATE -> result.withInsertHandler(HtlIdentifierInsertHandler())

            DATA_SLY_UNWRAP -> result

            else -> result.withInsertHandler(XmlAttributeInsertHandler())
        }
    }

}
