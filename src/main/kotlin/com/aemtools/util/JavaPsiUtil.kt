package com.aemtools.util

import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.constant.const.java.SLING_FILTER_ANNOTATION
import com.aemtools.constant.const.java.SLING_SERVLET_ANNOTATION
import com.aemtools.constant.const.java.FELIX_PROPERTY_ANNOTATION
import com.intellij.psi.*

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

    // TODO add check for OSGi declarative service
    return annotations().any {
        it.qualifiedName in listOf(FELIX_SERVICE_ANNOTATION,
                SLING_SERVLET_ANNOTATION,
                SLING_FILTER_ANNOTATION)
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
 * Get list of [PsiAnnotation] objects from current psi modifier list owner.
 *
 * @receiver [PsiModifierListOwner]
 * @return list of annotations
 */
fun PsiModifierListOwner.annotations(): List<PsiAnnotation> =
        modifierList?.children?.map { it as? PsiAnnotation }
                ?.filterNotNull()
                ?: emptyList()

/**
 * Find all methods which may used from EL
 *
 * The suitable method should be public and should not contain any arguments.
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
 * Collect methods grouped by declaration class.
 * Classes are sorted from the deepest one up to `Object` class.
 *
 * @receiver [PsiClass]
 * @return map with methods grouped by declaring class and keys sorted by inheritance depth (from the deepest one to `Object`)
 */
fun PsiClass.methodsSortedByClass(): LinkedHashMap<PsiClass, List<PsiMethod>> = this.let {
    var currentClass: PsiClass? = this
    val result: LinkedHashMap<PsiClass, List<PsiMethod>> = LinkedHashMap()
    while (currentClass != null) {
        val methods = currentClass.methods.filter { classMethod ->
            classMethod.isProperForHtlEl()
                    && !result.flatMap { it.value }.any { it.name == classMethod.name }
        }
        result.put(currentClass, methods)
        currentClass = currentClass.superClass
    }
    result
}

/**
 * Check if current method is suitable for usage in Htl EL.
 *
 * @receiver [PsiMethod]]
 * @return *true* if current method may be invoked from Htl EL, *false* otherwise
 */
fun PsiMethod.isProperForHtlEl(): Boolean = !this.isConstructor
        && hasModifierProperty(PsiModifier.PUBLIC)
        && parameterList.parameters.isEmpty()
        && !returnType!!.isAssignableFrom(PsiType.VOID)

/**
 * Check if current field is sutable for usage in Htl EL.
 *
 * @receiver [PsiField]
 * @return *true* if current method may be used from Htl EL, *false* otherwise
 */
fun PsiField.isProperForHtlEl(): Boolean = hasModifierProperty(PsiModifier.PUBLIC)
        && !hasModifierProperty(PsiModifier.STATIC)

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

fun PsiClass.fieldsSortedByClass(): LinkedHashMap<PsiClass, List<PsiField>> = this.let {
    var currentClass: PsiClass? = this
    val result: LinkedHashMap<PsiClass, List<PsiField>> = LinkedHashMap()
    while (currentClass != null) {
        val fields = this.fields.filter { it.hasModifierProperty(PsiModifier.PUBLIC) }
        result.put(currentClass, fields)
        currentClass = currentClass.superClass
    }
    result
}

/**
 * Find all Htl EL compatible members.
 *
 * @return the sum of [elFields] and [elMembers]
 */
fun PsiClass.elMembers(): List<PsiMember> {
    return this.elMethods() + this.elFields()
}

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
        startsWith("is") -> substring(2).decapitalize()
        startsWith("get") -> substring(3).decapitalize()
        else -> this
    }
}