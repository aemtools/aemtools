package com.aemtools.util

import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.constant.const.java.SLING_SERVLET_ANNOTATION
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiType

/**
 * @author Dmytro_Troynikov
 */

/**
 * Check if current [PsiClass] is an OSGi service.
 *
 * @receiver [PsiClass]
 * @return *true* if class is marked with corresponding OSGi annotations, *false* otherwise
 */
fun PsiClass.isOSGiService(): Boolean {
  val annotations = this.modifierList?.children?.map {
    it as? PsiAnnotation
  }?.filterNotNull()
      ?: return false

  // TODO add check for OSGi declarative service
  return annotations.any {
    it.qualifiedName in listOf(FELIX_SERVICE_ANNOTATION,
        SLING_SERVLET_ANNOTATION,
        SLING_FILTER_ANNOTATION)
  }
}

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
    startsWith("is") -> substringAfter("is").decapitalize()
    startsWith("get") -> substringAfter("get").decapitalize()
    else -> this
  }
}
