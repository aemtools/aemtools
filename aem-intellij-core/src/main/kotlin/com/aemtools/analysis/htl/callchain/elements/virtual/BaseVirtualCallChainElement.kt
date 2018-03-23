package com.aemtools.analysis.htl.callchain.elements.virtual

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.ArrayJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.IterableJavaTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.java.MapJavaTypeDescriptor
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * Base implementation of [VirtualCallChainElement].
 *
 * @author Dmytro Primshyts
 */
class BaseVirtualCallChainElement(
    override val name: String,
    override val type: TypeDescriptor,
    override val previous: VirtualCallChainElement? = null
) : VirtualCallChainElement {
  override fun toLookupElement(): LookupElement {
    val lookupString = buildString {
      append(name)

      var _previous = previous
      while (_previous != null) {
        insert(0, "${_previous.name}.")
        _previous = _previous.previous
      }
    }

    var lookupElement = LookupElementBuilder.create(lookupString)

    when (type) {
      is ArrayJavaTypeDescriptor -> {
        lookupElement = lookupElement.withTailText(type.psiClass.qualifiedName)
        lookupElement = lookupElement.withIcon(type.psiClass.getIcon(0))
      }
      is IterableJavaTypeDescriptor -> {
        val memberName = type.psiMember?.name
        if (memberName != null) {
          lookupElement = lookupElement.withTailText(" $lookupString", true)
          lookupElement = lookupElement.withPresentableText(memberName)
        }

        val typeText = type.originalType?.presentableText
        if (typeText != null) {
          lookupElement = lookupElement.withTypeText(typeText)
        }

        lookupElement = lookupElement.withIcon(type.psiClass.getIcon(0))
      }
      is MapJavaTypeDescriptor -> {
        lookupElement = lookupElement.withTailText(type.psiClass.qualifiedName)
        lookupElement = lookupElement.withIcon(type.psiClass.getIcon(0))
      }
      else -> Unit
    }

    return lookupElement
  }
}
