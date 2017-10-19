package com.aemtools.analysis.htl.callchain.typedescriptor.java

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.aemtools.completion.htl.CompletionPriority
import com.aemtools.completion.htl.model.ResolutionResult
import com.aemtools.constant.const
import com.aemtools.lang.java.JavaSearch
import com.aemtools.util.allScope
import com.aemtools.util.elFields
import com.aemtools.util.elMethods
import com.aemtools.util.elName
import com.aemtools.util.findElMemberByName
import com.aemtools.util.psiManager
import com.aemtools.util.resolveReturnType
import com.aemtools.util.withPriority
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
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
 * @author Dmytro_Troynikov
 */
open class JavaPsiClassTypeDescriptor(open val psiClass: PsiClass,
                                      open val psiMember: PsiMember? = null,
                                      open val originalType: PsiType? = null) : TypeDescriptor {
  override fun isArray(): Boolean = originalType is PsiArrayType

  override fun isIterable(): Boolean {
    val iterableInterface = JavaSearch.findClass("java.lang.Iterable", psiClass.project) ?: return false

    return this.psiClass.isInheritor(iterableInterface, true)
        || this.psiClass.isEquivalentTo(iterableInterface)
  }

  override fun isMap(): Boolean {
    val mapClass = JavaSearch.findClass("java.util.Map", psiClass.project) ?: return false

    return this.psiClass.isInheritor(mapClass, true)
        || this.psiClass.isEquivalentTo(mapClass)
  }

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

      result.add(lookupElement.withPriority(extractPriority(it, psiClass)))
    }
    fields.forEach {
      val lookupElement = LookupElementBuilder.create(it.name.toString())
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
          val iterable = JavaSearch.findClass(const.java.ITERABLE, psiClass.project)
          val iterator = JavaSearch.findClass(const.java.ITERATOR, psiClass.project)
          val map = JavaSearch.findClass(const.java.MAP, psiClass.project)

          if (iterable != null && map != null && iterator != null) {
            if (psiClass.isInheritor(iterable, true)
                || psiClass.isEquivalentTo(iterable)) {
              return IterableJavaTypeDescriptor(psiClass, psiMember, psiType)
            }

            if (psiClass.isInheritor(iterator, true)
                || psiClass.isEquivalentTo(iterator)) {
              return IterableJavaTypeDescriptor(psiClass, psiMember, psiType)
            }

            if (psiClass.isInheritor(map, true)
                || psiClass.isEquivalentTo(map)) {
              return MapJavaTypeDescriptor(psiClass, psiMember, psiType)
            }

          }

          JavaPsiClassTypeDescriptor(psiClass, psiMember, psiType)
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
