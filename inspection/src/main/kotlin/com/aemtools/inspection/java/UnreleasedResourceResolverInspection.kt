package com.aemtools.inspection.java

import com.aemtools.common.util.findParentByType
import com.aemtools.common.util.parameterOfType
import com.aemtools.inspection.common.AemIntellijInspection
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiAnonymousClass
import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiCallExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionList
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiLambdaExpression
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMember
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiReturnStatement
import com.intellij.psi.PsiThisExpression
import com.intellij.psi.PsiVariable
import com.intellij.psi.impl.source.tree.java.PsiAssignmentExpressionImpl
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import com.siyeh.ig.psiutils.ParenthesesUtils
import com.siyeh.ig.psiutils.TypeUtils

/**
 * AEM 6 implementation.
 *
 * ResourceResolver creation will be marked in following cases:
 *
 * #1
 *
 * ```
 * void foo() {
 *   createResourceResolver();
 * }
 * ```
 *
 * #2
 *
 * ```
 * void foo() {
 *   ResourceResolver resolver = createResourceResolver();
 *
 *   bar(resolver);
 * }
 *
 * void bar(ResourceResolver resolver) {
 *   // do some stuff with resolver, leave resolver unclosed
 * }
 * ```
 *
 * @author Dmytro Troynikov
 */
class UnreleasedResourceResolverInspection : AemIntellijInspection(
    name = "Unrealeased ResourceResolver inspection",
    groupName = "AEM",
    description = """
      This inspection checks that <i>org.apache.sling.api.resource.ResourceResolver</i>
      is closed.
    """
) {
  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : JavaElementVisitor() {
      override fun visitExpression(expression: PsiExpression) {
        val isResourceResolverCreation = isResourceResolverCreation(expression)

        if (!isResourceResolverCreation) {
          return
        }

        val variable = getVariable(expression)
        val localVariable = getLocalVariable(expression)

        if (variable == null && localVariable == null
            && !isResourceEscapesCurrentMethod(expression, variable)
            && !isAssignedToField(expression, variable)) {
          // resolver created, unused - memory leak
          registerWarning(holder, expression)
        }

        if (variable == null) {
          return
        }

        val containerClass = variable.findParentByType(PsiClass::class.java)
            ?: return

        if (!isResourceEscapesCurrentMethod(expression, variable)
            && !isResourceClosed(variable)) {
          registerWarning(holder, expression)
        }
      }
    }
  }

  /**
   * Check if given variable and/or expression is assigned to field
   *
   * @param expression the expression
   * @param variable the variable
   * @return *true* if the expression or variable is assigned to field,
   * *false* otherwise
   */
  private fun isAssignedToField(expression: PsiExpression, variable: PsiVariable?): Boolean {
    if (variable == null) {
      if (expression is PsiAssignmentExpressionImpl) {
        val lExpression = expression.lExpression
        if (lExpression.firstChild is PsiThisExpression) {
          return true
        }

        if (lExpression is PsiReferenceExpressionImpl) {
          val declaration = lExpression.resolve()

          if (declaration is PsiField) {
            return true
          }
        }
      }
    }
    return false
  }

  private fun isResourceClosed(variable: PsiElement): Boolean {
    val refs = ReferencesSearch.search(variable).findAll()

    refs.mapNotNull { it.element.findParentByType(PsiMethodCallExpression::class.java) }
        .forEach {

          val methodExpression = it.methodExpression
          // check if the variable is closed in current method
          if (closeInvocation(methodExpression)) {
            return true
          }

          val resolved = methodExpression.resolve() as? PsiMethod
              ?: return@forEach

          val param = resolved.parameterOfType("org.apache.sling.api.resource.ResourceResolver")
          if (param != null && isResourceClosed(param)) {
            return true
          }
        }

    return false
  }

  private fun closeInvocation(expression: PsiReferenceExpression): Boolean {
    return "close" == expression.referenceName
  }

  private fun isResourceResolverCreation(expression: PsiExpression): Boolean {
    return TypeUtils.expressionHasTypeOrSubtype(
        expression,
        "org.apache.sling.api.resource.ResourceResolver"
    )
  }

  private fun isResourceEscapesCurrentMethod(
      resourceCreationExpression: PsiExpression,
      variable: PsiVariable?)
      : Boolean {
    var parent = ParenthesesUtils.getParentSkipParentheses(resourceCreationExpression)

    if (parent is PsiConditionalExpression) {
      parent = ParenthesesUtils.getParentSkipParentheses(resourceCreationExpression)
    }

    if (parent is PsiReturnStatement) {
      return true
    }

    if (parent is PsiAssignmentExpression) {
      if (ParenthesesUtils.stripParentheses(parent) != resourceCreationExpression) {
        return true
      }

      val lhs = ParenthesesUtils.stripParentheses(parent.lExpression)

      if (lhs is PsiReferenceExpression) {
        val target = lhs.resolve()
        if (target is PsiField) {
          return true
        }
      }
    } else if (parent is PsiExpressionList) {
      var grandParent = parent.parent
      if (grandParent is PsiAnonymousClass) {
        grandParent = grandParent.parent
      }

      if (grandParent is PsiCallExpression) {
        return isResourceResolverCreation(grandParent)
      }
    }

    if (variable == null) {
      return false
    }

    val codeBlock = PsiTreeUtil.getParentOfType(
        resourceCreationExpression,
        PsiCodeBlock::class.java,
        true,
        PsiMember::class.java
    ) ?: return false

    val escapeVisitor = EscapeVisitor(variable)

    codeBlock.accept(escapeVisitor)

    return escapeVisitor.isEscaped
  }

  private fun registerWarning(holder: ProblemsHolder, expression: PsiExpression) {
    holder.registerProblem(expression,
        "Unclosed ResourceResolver",
        ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
  }

  fun getVariable(expression: PsiExpression): PsiVariable? {
    val parent = getParent(expression)
    if (parent is PsiAssignmentExpression) {
      val lhs = parent.lExpression as? PsiReferenceExpression ?: return null
      val referent = lhs.resolve()
      return referent as? PsiVariable
    } else {
      return parent as? PsiVariable
    }
  }

  fun getLocalVariable(expression: PsiExpression): PsiLocalVariable? {
    val parent = getParent(expression)
    if (parent is PsiExpressionStatement) {
      val ref = parent.firstChild?.firstChild as? PsiReferenceExpression
          ?: return null

      return ref.resolve() as? PsiLocalVariable
    }
    return null
  }

  private fun getParent(expression: PsiExpression): PsiElement? {
    var parent: PsiElement = ParenthesesUtils.getParentSkipParentheses(expression) ?: return null
    var grandParent: PsiElement = parent.parent
    val type = expression.type ?: return null
    while (parent is PsiReferenceExpression && grandParent is PsiMethodCallExpression) {
      if (type != grandParent.type) {
        return null
      }
      parent = ParenthesesUtils.getParentSkipParentheses(grandParent) ?: return null
      grandParent = parent.parent
    }
    return parent
  }

  private inner class EscapeVisitor(private val boundVariable: PsiVariable) : JavaRecursiveElementWalkingVisitor() {
    var isEscaped: Boolean = false
      private set

    override fun visitLambdaExpression(expression: PsiLambdaExpression) {}

    override fun visitClass(aClass: PsiClass) {}

    override fun visitElement(element: PsiElement?) {
      if (isEscaped) {
        return
      }
      super.visitElement(element)
    }

    override fun visitReturnStatement(statement: PsiReturnStatement) {
      super.visitReturnStatement(statement)
      val value = PsiUtil.deparenthesizeExpression(statement.returnValue) as? PsiReferenceExpression ?: return
      val target = value.resolve()
      if (boundVariable == target) {
        isEscaped = true
      }
    }

    override fun visitAssignmentExpression(expression: PsiAssignmentExpression) {
      super.visitAssignmentExpression(expression)
      val rhs = PsiUtil.deparenthesizeExpression(expression.rExpression) as? PsiReferenceExpression ?: return
      val target = rhs.resolve()
      if (boundVariable != target) {
        return
      }
      val lhs = PsiUtil.deparenthesizeExpression(expression.lExpression) as? PsiReferenceExpression ?: return
      val lTarget = lhs.resolve()
      if (lTarget is PsiField) {
        isEscaped = true
      }
    }

    override fun visitCallExpression(callExpression: PsiCallExpression) {
      if (!isResourceResolverCreation(callExpression)) {
        return
      }
      val argumentList = callExpression.argumentList ?: return
      val expressions = argumentList.expressions
      for (expression in expressions) {
        val expression1 = PsiUtil.deparenthesizeExpression(expression) as? PsiReferenceExpression ?: continue
        val target = expression1.resolve()
        if (boundVariable == target) {
          isEscaped = true
          break
        }
      }
    }
  }

}

