package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.common.completion.lookupElement
import com.aemtools.common.completion.withPriority
import com.aemtools.common.util.allScope
import com.aemtools.common.util.elFields
import com.aemtools.common.util.elMethods
import com.aemtools.common.util.elName
import com.aemtools.common.util.findElMemberByName
import com.aemtools.common.util.psiManager
import com.aemtools.common.util.resolveReturnType
import com.aemtools.completion.htl.CompletionPriority
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.lang.java.JavaSearch
import com.aemtools.lang.java.JavaUtilities
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import java.util.ArrayList

/**
 * Type descriptor which uses given [PsiClass] to provide type information.
 *
 * @author Dmytro Primshyts
 */
open class JavaPsiClassTypeDescriptor(open val psiClass: PsiClass,
                                      open val psiMember: PsiMember? = null,
                                      open val originalType: PsiType? = null) : TypeDescriptor {
  override fun isArray(): Boolean = originalType is PsiArrayType

  override fun isIterable(): Boolean = JavaUtilities.isIterable(psiClass)

  override fun isMap(): Boolean = JavaUtilities.isMap(psiClass)

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
      var lookupElement = lookupElement(name)
          .withIcon(it.getIcon(0))
          .withTailText(" ${it.name}()", true)

      val returnType = it.returnType
      if (returnType != null) {
        lookupElement = lookupElement.withTypeText(returnType.presentableText, true)
      }

      result.add(lookupElement.withPriority(extractPriority(it, psiClass)))
    }
    fields.forEach {
      val lookupElement = lookupElement(it.name.toString())
          .withIcon(it.getIcon(0))
          .withTypeText(it.type.presentableText, true)

      result.add(lookupElement.withPriority(extractPriority(it, psiClass)))
    }

    return result
  }

  private fun extractPriority(member: PsiMember, clazz: PsiMember) = when {
    member.containingClass == clazz ->
      CompletionPriority.CONTAINING_CLASS
    member.containingClass?.qualifiedName == "java.lang.Object" ->
      CompletionPriority.OBJECT_CLASS
    else -> CompletionPriority.MIDDLE_CLASS
  }

  override fun subtype(identifier: String): TypeDescriptor {
    val psiMember = psiClass.findElMemberByName(identifier)
        ?: return TypeDescriptor.empty()

    val psiType = psiMember.resolveReturnType()
        ?: return TypeDescriptor.empty()

    val className = with(psiType) {
      when {
        this is PsiClassReferenceType -> {
          this.rawType().canonicalText
        }
        this is PsiClassType -> this.className
        this is PsiPrimitiveType -> this.getBoxedType(
            psiClass.project.psiManager(),
            psiClass.project.allScope()
        )?.canonicalText
        this is PsiArrayType -> {
          this.componentType.canonicalText
        }
        else -> null
      }
    } ?: return TypeDescriptor.empty()

    val typeClass = JavaSearch.findClass(className, psiClass.project)
        ?: return TypeDescriptor.unresolved(psiMember)

    return JavaPsiClassTypeDescriptor.create(typeClass, psiMember, psiType)
  }

  override fun referencedElement(): PsiElement? =
      psiMember

  override fun asResolutionResult(): ResolutionResult =
      ResolutionResult(psiClass, myVariants())

  fun qualifiedName(): String? = psiClass.qualifiedName

  companion object {
    /**
     * Build method for [JavaPsiClassTypeDescriptor].
     *
     * @param psiClass the psi class
     * @param psiMember the psi member (_null_ by default)
     * @param psiType the psi type (_null_ by default)
     *
     * @return new java psi class type descriptor
     */
    fun create(psiClass: PsiClass,
               psiMember: PsiMember? = null,
               psiType: PsiType? = null): JavaPsiClassTypeDescriptor {
      return when (psiType) {
        is PsiClassReferenceType -> {
          when {
            JavaUtilities.isIterable(psiClass)
                || JavaUtilities.isIterator(psiClass) ->
              IterableJavaTypeDescriptor(psiClass, psiMember, psiType)

            JavaUtilities.isMap(psiClass) ->
              MapJavaTypeDescriptor(psiClass, psiMember, psiType)

            else -> JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
          }
        }
        is PsiClassType,
        is PsiPrimitiveType ->
          JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
        is PsiArrayType ->
          ArrayJavaTypeDescriptor(psiClass, psiMember, psiType)
        else -> JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
      }
    }
  }

}
