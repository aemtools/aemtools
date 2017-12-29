package com.aemtools.inspection.common

import com.intellij.codeInspection.BatchSuppressableTool
import com.intellij.codeInspection.LocalInspectionTool

/**
 * Base inspection.
 *
 * @author Dmytro Troynikov
 */
abstract class AemIntellijInspection(
    val groupName: String,
    val name: String,
    val description: String
) : LocalInspectionTool(), BatchSuppressableTool {
  override fun getGroupDisplayName(): String = groupName
  override fun getDisplayName(): String = name
  override fun getStaticDescription(): String? = description
}
