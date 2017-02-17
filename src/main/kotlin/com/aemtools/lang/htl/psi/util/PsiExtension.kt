package com.aemtools.lang.htl.psi.util

import com.aemtools.completion.htl.model.DeclarationType
import com.aemtools.lang.java.JavaSearch
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTokenType

/**
* @author Dmytro Troynikov
*/

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
 * Find all fields which may be used from EL.
 *
 * The suitable fields should be public
 */
fun PsiClass.elFields(): List<PsiField> = this.allFields
        .filter {
            it.hasModifierProperty(PsiModifier.PUBLIC)
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
fun PsiClass.findElMemberByName(name: String) : PsiMember? = elMembers().find {
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

/**
 * Check if current [PsiElement] resides within the body of given [XmlTag]
 *  e.g. given element - this, div - the tag to compare against
 *
 *  ```
 *      <div>  <element>  <div> --> true
 *
 *      <div attribute="<element>"> ... </div> --> false
 *
 *      <div></div> <element> --> false
 *  ```
 *
 *  @return __true__ if current element is situated within given tag's body.
 */
fun PsiElement.isWithin(tag: XmlTag): Boolean {
    val composite = tag as CompositeElement
    val tagEnd = composite.findChildByType(XmlTokenType.XML_TAG_END) ?: return false
    val endTagStart = composite.findChildByType(XmlTokenType.XML_END_TAG_START) ?: return false

    val leftBorder = tagEnd.startOffset + 1
    val rightBorder = endTagStart.startOffset

    return textOffset > leftBorder && textOffset < rightBorder
}

/**
 * Check if current [PsiElement] is not within the body of give [XmlTag]
 * (inverted version of [isWithin])
 */
fun PsiElement.isNotWithin(tag: XmlTag): Boolean = !isWithin(tag)

/**
 * Check if current [PsiElement] is part of given [XmlTag]
 *  e.g. given: element - this, div - the tag to check:
 *
 *  ```
 *      <div> <element> </div> --> true
 *      <div attribute="<element>"></div> --> true
 *      <div></div> <element> --> false
 *  ```
 *
 *  @return __true__ in case if current element is part given tag
 */
fun PsiElement.isPartOf(element: XmlElement): Boolean {
    return textOffset > element.textOffset && textOffset < (element.textOffset + element.textLength)
}

/**
 * Check if current element is not part of given element.
 * (inverted version of [isPartOf])
 */
fun PsiElement.isNotPartOf(element: XmlElement): Boolean {
    return !isPartOf(element)
}