package com.aemtools.inspection.service

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project


/**
 * Run given action with required service.
 * As a required service may be specified:
 *
 * * [IInspectionService]
 * * [IJavaInspectionService]
 *
 * @param project the project
 * @param action action to be executed with requested service
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified SERVICE> withServices(project: Project, action: (service: SERVICE) -> Unit) {
  val service = when (SERVICE::class.java) {
    IInspectionService::class.java -> InspectionService.getInstance(project)
    IJavaInspectionService::class.java -> JavaInspectionService.getInstance(project)
    else -> null
  }

  if (service == null) {
    Logger.getInstance("aemtools")
        .warn("Unable to execute action with service: ${SERVICE::class.java.name}")
    return
  }

  action.invoke(service as SERVICE)
}
