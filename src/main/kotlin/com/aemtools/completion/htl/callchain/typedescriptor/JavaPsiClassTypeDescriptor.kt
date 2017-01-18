package com.aemtools.completion.htl.callchain.typedescriptor

import com.aemtools.lang.htl.psi.util.elFields
import com.aemtools.lang.htl.psi.util.elMethods
import com.aemtools.lang.htl.psi.util.elName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiClass
import java.util.*

/**
 * Type descriptor which uses given [PsiClass] to provide type information.
 * @author Dmytro_Troynikov
 */
class JavaPsiClassTypeDescriptor(val psiClass: PsiClass) : TypeDescriptor() {

    override fun myVariants(): List<LookupElement> {
        val methods = psiClass.elMethods()
        val fields = psiClass.elFields()

        val methodNames = ArrayList<String>()
        val result = ArrayList<LookupElement>()

        methods.forEach {
            var name = it.elName()
            if (methodNames.contains(name)) {
                name = it.name
            } else {
                methodNames.add(name)
            }
            var lookupElement = LookupElementBuilder.create(name)
                    .withIcon(it.getIcon(0))
                    .withTailText(" ${it.name}()", true)

            val returnType = it.returnType
            if (returnType != null) {
                lookupElement = lookupElement.withTypeText(returnType.presentableText, true)
            }

            result.add(lookupElement)
        }

        fields.forEach {
            val lookupElement = LookupElementBuilder.create(it.name.toString())
                    .withIcon(it.getIcon(0))
                    .withTypeText(it.type.presentableText, true)

            result.add(lookupElement)
        }

        return result
    }

    override fun subtype(identifier: String): TypeDescriptor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}