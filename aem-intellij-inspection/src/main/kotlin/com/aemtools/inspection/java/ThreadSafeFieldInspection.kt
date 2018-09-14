package com.aemtools.inspection.java

import com.aemtools.common.util.annotations
import com.aemtools.common.util.resolveReturnType
import com.aemtools.common.util.toPsiClass
import com.aemtools.inspection.common.AemIntellijInspection
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiField
import com.intellij.psi.search.GlobalSearchScope

/**
 * AEM 3 rule implementation.
 *
 * @author Dmytro Troynikov
 */
class ThreadSafeFieldInspection : AemIntellijInspection(
    name = "Thread-safe field inspection",
    groupName = "AEM",
    description = """
      This inspection checks that <b>Non</b>-threadsafe
      classes aren't used as fields in singleton classes.
      (e.g. filters or servlets)
    """
) {
  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor() {
      override fun visitField(field: PsiField) {
        val containerClass = field.containingClass ?: return

        if (isVulnerableClass(containerClass)
            && isVulnerableField(field)) {
          holder.registerProblem(
              field,
              "Non-thread safe field.",
              ProblemHighlightType.ERROR
          )
        }
      }
    }
  }

  private fun isVulnerableClass(psiClass: PsiClass): Boolean {
    val module = ModuleUtil.findModuleForPsiElement(psiClass) ?: return false
    val jpf = JavaPsiFacade.getInstance(psiClass.project)

    val interfaces = vulnerableInterfaces.mapNotNull { inter ->
      jpf.findClass(inter, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module))
    }

    return psiClass.annotations().any { annotation ->
      annotation.qualifiedName in vulnerableAnnotations
    } || interfaces.any { psiClass.isInheritorDeep(it, null) }
  }

  private fun isVulnerableField(psiField: PsiField): Boolean {
    val fqn = psiField.resolveReturnType()?.toPsiClass()?.qualifiedName
        ?: return false

    return fqn in nonThreadSaveTypes
  }

  companion object {
    val nonThreadSaveTypes = setOf(
        "org.apache.sling.api.resource.ResourceResolver",
        "javax.jcr.Session",
        "com.day.cq.wcm.api.PageManager",
        "com.day.cq.wcm.api.components.ComponentManager",
        "com.day.cq.wcm.api.designer.Designer",
        "com.day.cq.dam.api.AssetManager",
        "com.day.cq.tagging.TagManager",
        "com.day.cq.security.UserManager",
        "org.apache.jackrabbit.api.security.user.Authorizable",
        "org.apache.jackrabbit.api.security.user.User",
        "org.apache.jackrabbit.api.security.user.UserManager"
    )

    val vulnerableInterfaces = setOf(
        "javax.servlet.Servlet",
        "javax.servlet.Filter",
        "org.osgi.service.event.EventHandler"
    )

    val vulnerableAnnotations = setOf(
        "org.apache.felix.scr.annotations.Component",
        "org.osgi.service.component.annotations.Component",
        "org.apache.felix.scr.annotations.sling.SlingServlet",
        "org.apache.felix.scr.annotations.sling.SlingFilter"
    )
  }
}
