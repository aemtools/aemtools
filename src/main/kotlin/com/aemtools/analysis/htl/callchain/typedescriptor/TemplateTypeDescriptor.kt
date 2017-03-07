package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.index.TemplateDefinition
import com.intellij.codeInsight.lookup.LookupElement

/**
 * @author Dmytro Troynikov
 */
class TemplateTypeDescriptor(val template: TemplateDefinition)
    : TypeDescriptor {
    override fun myVariants(): List<LookupElement> {
        return listOf()
    }

    override fun subtype(identifier: String): TypeDescriptor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun name(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isArray(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isIterable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isMap(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}