package com.aemtools.inspection.html

import com.aemtools.common.constant.const
import com.aemtools.common.util.hasChild
import com.aemtools.inspection.service.IInspectionService
import com.aemtools.inspection.service.IJavaInspectionService
import com.aemtools.inspection.service.InspectionService
import com.aemtools.inspection.service.JavaInspectionService
import com.aemtools.lang.htl.psi.HtlContextExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlPropertyAccess
import com.aemtools.lang.htl.psi.HtlStringLiteral
import com.aemtools.lang.util.isInsideOF
import com.intellij.codeInspection.BatchSuppressableTool
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

/**
 * @author Dmytro Troynikov
 */
class RedundantElInspection : AemIntellijInspection(
    groupName = "HTL",
    name = "Redundant HTL expression",
    description = """
      This inspection checks if HTL expression is used in `data-sly-use` or `data-sly-include` attributes
      without reason. Expression may be required in case if there is necessity to pass some arguments
      via "option" arguments, otherwise expression is redundant
    """,
    inspection = inspection<HtlHtlEl> { element, problemsHolder ->
      withServices<IInspectionService>(element.project) { inspectionService ->

        if (hasDefect(element)) {
          inspectionService.reportRedundantEl(element, problemsHolder)
        }
      }
    }
) {
}

@Suppress("IMPLICIT_CAST_TO_ANY")
private inline fun <reified SERVICE> withServices(project: Project, action: (service: SERVICE) -> Unit): Unit {
  val service = when (SERVICE::class.java) {
    is IInspectionService -> InspectionService.getInstance(project)
    is IJavaInspectionService -> JavaInspectionService.getInstance(project)
    else -> null
  }

  if (service == null) {
    Logger.getInstance("aemtools")
        .warn("Unable to execute action with service: ${SERVICE::class.java.name}")
    return
  }

  action.invoke(service as SERVICE)
}

private fun hasDefect(element: HtlHtlEl): Boolean =
    element.isDumbStringLiteralEl()
        && (element.isInsideOF(const.htl.DATA_SLY_USE) || element.isInsideOF(const.htl.DATA_SLY_INCLUDE))

/**
 * Check if current current [HtlHtlEl] is a "Dumb String Literal", which mean
 * that the expression doesn't contain anything except the string literal, e.g.:
 * ```
 *   ${'static string'}
 * ```
 */
private fun HtlHtlEl.isDumbStringLiteralEl(): Boolean =
    this.hasChild(HtlStringLiteral::class.java)
        && !this.hasChild(HtlPropertyAccess::class.java)
        && !this.hasChild(HtlContextExpression::class.java)

abstract class AemIntellijInspection(
    val groupName: String,
    val name: String,
    val description: String,
    val inspection: IInspectionAction
) : LocalInspectionTool(), BatchSuppressableTool {
  override fun getGroupDisplayName(): String = groupName
  override fun getDisplayName(): String = name
  override fun getStaticDescription(): String? = description

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : PsiElementVisitor() {
      override fun visitElement(element: PsiElement?) {
        if (inspection is InspectionAction && element != null) {
          val clazz = inspection.clazz

          try {
            clazz.java.cast(element)?.let { casted->
              inspection::class.memberFunctions
                  .find { it.name == "invoke" }
                  ?.call(casted, holder)
            }
          } catch (e: Exception) {}

        }
      }
    }
  }
}

inline fun <reified T : PsiElement> inspection(noinline action: (element: T, problemsHolder: ProblemsHolder) -> Unit)
    : IInspectionAction =
    InspectionAction(T::class, action)

interface IInspectionAction {

  fun <ELEMENT : PsiElement> invoke(element: ELEMENT, holder: ProblemsHolder): Unit

}

class InspectionAction(
    val clazz: KClass<Any>,
    val action: (element: PsiElement, problemsHolder: ProblemsHolder) -> Unit)
  : IInspectionAction {
  override fun <ELEMENT : PsiElement> invoke(element: ELEMENT, holder: ProblemsHolder) =
      action.invoke(element, holder)
}

