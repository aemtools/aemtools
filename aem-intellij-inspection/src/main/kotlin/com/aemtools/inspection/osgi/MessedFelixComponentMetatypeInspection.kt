package com.aemtools.inspection.osgi

import com.aemtools.common.constant.const
import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.isFelixProperty
import com.aemtools.inspection.common.AemIntellijInspection
import com.aemtools.inspection.osgi.fix.RemoveAnnotationAttributeAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiField

/**
 * @author Kostiantyn Diachenko
 */
class MessedFelixComponentMetatypeInspection : AemIntellijInspection(
    groupName = "OSGi",
    name = "Messed Felix Component metatype",
    description = """
       This inspection checks that <i>@Component</i> is defined to generate metatype information, 
       however no properties or only private properties have been defined; 
       in case no properties or only private properties are wanted, consider to use 'metatype=false'
    """
) {

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor() {
      override fun visitAnnotation(annotation: PsiAnnotation) {
        if (const.java.FELIX_COMPONENT_ANNOTATION != annotation.qualifiedName) {
          return
        }
        val containerClass = annotation.findParentByType(PsiClass::class.java) ?: return
        checkAnnotation(annotation, containerClass, holder)
      }
    }
  }

  private fun checkAnnotation(annotation: PsiAnnotation, containerClass: PsiClass, holder: ProblemsHolder) {
    val metadataAttribute = annotation.findAttributeValue("metatype") ?: return
    if (!metadataAttribute.text.toBoolean()) {
      return
    }

    val notPrivateClassLevelProperties = getClassLevelDefinedProperties(containerClass)
    val notPrivateFieldLevelProperties = getFieldLevelDefinedProperties(containerClass)

    if (notPrivateClassLevelProperties.isEmpty() && notPrivateFieldLevelProperties.isEmpty()) {
      holder.registerProblem(
          metadataAttribute,
          "In case no properties or only private properties are wanted, consider to use 'metatype=false'",
          ProblemHighlightType.GENERIC_ERROR,
          RemoveAnnotationAttributeAction("Remove 'metatype' attribute")
      )
    }
  }

  private fun getClassLevelDefinedProperties(containerClass: PsiClass): List<PsiAnnotation> =
      containerClass.annotations
          .firstOrNull { it.hasQualifiedName(const.java.FELIX_PROPERTIES_ANNOTATION) }
          ?.findChildrenByType(PsiAnnotation::class.java)
          ?.filter {
            it.hasQualifiedName(const.java.FELIX_PROPERTY_ANNOTATION) && isNotPrivateProperty(it)
          } ?: emptyList()

  private fun getFieldLevelDefinedProperties(containerClass: PsiClass): List<PsiField> =
      containerClass.allFields.filter {
        it.isFelixProperty() && isNotPrivateProperty(it.getAnnotation(const.java.FELIX_PROPERTY_ANNOTATION))
      }

  private fun isNotPrivateProperty(propertyAnnotation: PsiAnnotation?) =
      propertyAnnotation
          ?.findAttributeValue("propertyPrivate")
          ?.text.toBoolean().not()
}
