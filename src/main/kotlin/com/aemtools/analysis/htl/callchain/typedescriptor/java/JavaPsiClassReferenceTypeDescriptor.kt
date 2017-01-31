package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.ArrayTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.IterableTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.MapTypeDescriptor
import com.aemtools.analysis.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiType

/**
 * @author Dmytro Troynikov
 */
class JavaPsiClassReferenceTypeDescriptor(val psiType: PsiType, private val project: Project)
    : TypeDescriptor, ArrayTypeDescriptor, MapTypeDescriptor, IterableTypeDescriptor {

    override fun arrayType(): TypeDescriptor {
        val className = psiType.canonicalText.substring(0, psiType.canonicalText.indexOf("[") - 1)
        val psiClass = JavaSearch.findClass(className, project)
        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null, null)
        } else {
            TypeDescriptor.empty()
        }
    }

    override fun iterableType(): TypeDescriptor {
        val fullName = psiType.canonicalText

        val parameterClassName = Regex("<([\\w.]*)>").find(fullName)?.groupValues?.get(1)
                ?: return TypeDescriptor.empty()

        val psiClass = JavaSearch.findClass(parameterClassName, project)

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null, null)
        } else {
            TypeDescriptor.empty()
        }
    }

    override fun keyType(): TypeDescriptor {
        val fullName = psiType.canonicalText

        val keyClassName = Regex("<([\\w.]*),").find(fullName)?.groupValues?.get(1)
            ?: return TypeDescriptor.empty()

        val psiClass = JavaSearch.findClass(keyClassName, project)

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null, null)
        } else {
            TypeDescriptor.empty()
        }
    }

    override fun valueType(): TypeDescriptor {
        val fullName = psiType.canonicalText

        val valueClassName = Regex(", ([\\w.]*)>").find(fullName)?.groupValues?.get(1)
            ?: return TypeDescriptor.empty()

        val psiClass = JavaSearch.findClass(valueClassName, project)

        return if (psiClass != null) {
            JavaPsiClassTypeDescriptor(psiClass, null, null)
        } else {
            TypeDescriptor.empty()
        }
    }

    override fun myVariants(): List<LookupElement> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subtype(identifier: String): TypeDescriptor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun name(): String {
        return psiType.canonicalText
    }

    override fun isArray(): Boolean {
        return psiType.canonicalText.contains("[]")
    }

    override fun isIterable(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isMap(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}