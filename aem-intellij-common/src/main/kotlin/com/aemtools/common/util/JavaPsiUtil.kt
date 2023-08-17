package com.aemtools.common.util

import com.aemtools.common.constant.const.java.DS_ATTRIBUTE_DEFINITION_ANNOTATION
import com.aemtools.common.constant.const.java.DS_COMPONENT_ANNOTATION
import com.aemtools.common.constant.const.java.DS_OBJECT_CLASS_DEFINITION_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_PROPERTY_ANNOTATION
import com.aemtools.common.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_HEALTH_CHECK_ANNOTATION
import com.aemtools.common.constant.const.java.SLING_SERVLET_ANNOTATION
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiModifierListOwner
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTypesUtil
import java.util.*

/**
 * @author Dmytro Primshyts
 */

/**
 * Check if current [PsiClass] is an OSGi service (Felix, R6, R7).
 *
 * @receiver [PsiClass]
 * @return *true* if class is marked with corresponding OSGi annotations, *false* otherwise
 */
fun PsiClass.isOSGiService(): Boolean {

  return annotations().any {
    it.qualifiedName in listOf(
        FELIX_SERVICE_ANNOTATION,
        SLING_SERVLET_ANNOTATION,
        SLING_FILTER_ANNOTATION,
        SLING_HEALTH_CHECK_ANNOTATION,
        DS_COMPONENT_ANNOTATION
    )
  }
}

/**
 * Check if current field is Felix property
 * (annotated with [FELIX_PROPERTY_ANNOTATION]).
 *
 * @receiver [PsiField]
 * @return _true_ if current field is felix property
 */
fun PsiField.isFelixProperty(): Boolean =
    annotations().any {
      it.qualifiedName == FELIX_PROPERTY_ANNOTATION
    }

/**
 * Check if current [PsiClass] is an OSGi Declarative Service (R6, R7).
 *
 * @receiver [PsiClass]
 * @return *true* if class is marked with corresponding OSGi annotations, *false* otherwise
 */
fun PsiClass.isDsOSGiConfig(): Boolean =
    annotations().any {
      it.qualifiedName == DS_OBJECT_CLASS_DEFINITION_ANNOTATION
    }

/**
 * Check if current method is OSGi DS config metadata property.
 * (annotated with [DS_ATTRIBUTE_DEFINITION_ANNOTATION]).
 *
 * @receiver [PsiMethod]
 * @return _true_ if current field is Object Class Definition method
 */
fun PsiMethod.isDsOSGiConfigProperty(): Boolean =
    annotations().any {
      it.qualifiedName == DS_ATTRIBUTE_DEFINITION_ANNOTATION
    }


/**
 * Get list of [PsiAnnotation] objects from current psi modifier list owner.
 *
 * @receiver [PsiModifierListOwner]
 * @return list of annotations
 */
fun PsiModifierListOwner.annotations(): List<PsiAnnotation> =
    modifierList?.annotations?.toList() ?: emptyList()

/**
 * Find all methods which may used from EL
 *
 * The suitable method should be public and should not contain any arguments.
 *
 * @receiver [PsiClass]
 * @return list of psi methods suitable for usage in EL
 */
fun PsiClass.elMethods(): List<PsiMethod> {
  val grouped: Map<PsiClass?, List<PsiMethod>> = this.allMethods.groupBy { it.containingClass }
  val myMethods = grouped[this] ?: listOf()

  return grouped.flatMap<PsiClass?, List<PsiMethod>, PsiMethod> {
    when {
      it.key == this@elMethods.containingClass -> listOf()
      else -> it.value.filter {
        !it.isConstructor
            && it.hasModifierProperty(PsiModifier.PUBLIC)
            && it.parameterList.parameters.isEmpty()
            && !it.returnType!!.isAssignableFrom(PsiType.VOID)
            && myMethods.find { myMethod -> it.name == myMethod.name } == null
      }
    }
  } + myMethods.filter {
    !it.isConstructor
        && it.hasModifierProperty(PsiModifier.PUBLIC)
        && it.parameterList.parameters.isEmpty()
        && !it.returnType!!.isAssignableFrom(PsiType.VOID)
  }
}

/**
 * Find all fields which may be used from EL.
 *
 * The suitable fields should be public
 */
fun PsiClass.elFields(): List<PsiField> = this.allFields
    .filter {
      it.hasModifierProperty(PsiModifier.PUBLIC)
          && !it.hasModifierProperty(PsiModifier.STATIC)
    }

/**
 * Find all Htl EL compatible members.
 *
 * @return the sum of [elFields] and [elMembers]
 */
fun PsiClass.elMembers(): List<PsiMember> = this.elMethods() + this.elFields()

/**
 * Find member (method or function) by name.
 * @param name the name of member
 * @return found [PsiMember] or _null_
 */
fun PsiClass.findElMemberByName(name: String): PsiMember? = elMembers().find {
  if (it is PsiMethod) {
    name == it.elName() || name == it.name
  } else {
    name == it.name
  }
}

/**
 * Get return [PsiType] of current [PsiMember].
 */
fun PsiMember.resolveReturnType(): PsiType? = when (this) {
  is PsiMethod -> this.returnType
  is PsiField -> this.type
  else -> null
}

/**
 * Normalize the method's name for Htl EL.
 *
 * Example:
 * ```
 *  "isEnabled" -> "enabled"
 *  "getName" -> "name"
 *  "color" -> "color"
 * ```
 * @return normalized method name
 */
fun PsiMethod.elName() = this.name.run {
  when {
    startsWith("is") -> substringAfter("is").replaceFirstChar { it.lowercase(Locale.getDefault()) }
    startsWith("get") -> substringAfter("get").replaceFirstChar { it.lowercase(Locale.getDefault()) }
    else -> this
  }
}

/**
 * Convert current [PsiType] into [PsiClass] if possible.
 *
 * @receiver [PsiType]
 * @return psi class
 * @see [PsiTypesUtil.getPsiClass]
 */
fun PsiType.toPsiClass() = PsiTypesUtil.getPsiClass(this)

/**
 * Check if current [PsiLiteralExpression] is `java.lang.String` literal.
 *
 * @receiver [PsiLiteralExpression]
 * @return *true* if current literal is java string literal,
 * *false* otherwise
 */
fun PsiLiteralExpression.isJavaLangString(): Boolean {
  val psiManager = PsiManager.getInstance(project)

  val myModule = ModuleUtil.findModuleForPsiElement(this) ?: return false

  return type == PsiType.getJavaLangString(
      psiManager,
      GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(myModule))
}
