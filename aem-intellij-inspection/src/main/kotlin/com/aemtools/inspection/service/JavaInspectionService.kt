package com.aemtools.inspection.service

import com.aemtools.common.util.isJavaLangString
import com.aemtools.common.util.toSmartPointer
import com.aemtools.inspection.java.constants.ConstantClasses
import com.aemtools.inspection.java.constants.ConstantDescriptor
import com.aemtools.inspection.java.fix.ReplaceHardcodedLiteralWithFqnAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author Dmytro Primshyts
 */
class JavaInspectionService : IJavaInspectionService {

  override fun reportHardcodedConstant(holder: ProblemsHolder,
                                       literal: PsiLiteralExpression,
                                       constantDescriptors: List<ConstantDescriptor>) {
    val fixes = constantDescriptors.map { constant ->
      ReplaceHardcodedLiteralWithFqnAction(
          "Replace with '${constant.containerClass}.${constant.name}'",
          constant,
          literal.toSmartPointer()
      )
    }

    holder.registerProblem(
        literal,
        "Hardcode of predefined constant",
        ProblemHighlightType.WEAK_WARNING,
        *fixes.toTypedArray()
    )
  }

  override fun standardConstants(project: Project, module: Module): List<ConstantDescriptor> {
    val jpf = JavaPsiFacade.getInstance(project)

    val variants = ConstantClasses.ALL.mapNotNull { fqn ->
      jpf.findClass(fqn, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module))
    }.flatMap { psiClass ->
      psiClass.allFields.mapNotNull { field ->
        val fqn = psiClass.qualifiedName ?: return@mapNotNull null
        val fieldName = field.name
        val fieldValue = field.computeConstantValue() as? String ?: return@mapNotNull null

        ConstantDescriptor(
            fqn,
            fieldName,
            fieldValue
        )
      }
    }.filterNot {
      it.value in ConstantClasses.EXLUSIONS
    }

    return variants
  }

  override fun isJavaLangString(psiLiteralExpression: PsiLiteralExpression): Boolean {
    return psiLiteralExpression.isJavaLangString()
  }

  companion object {

    /**
     * Get instance of [IJavaInspectionService].
     *
     * @param project the project
     * @return instance of java inspection service
     */
    fun getInstance(project: Project): IJavaInspectionService? {
      return ServiceManager.getService(project, IJavaInspectionService::class.java)
    }

  }

}
