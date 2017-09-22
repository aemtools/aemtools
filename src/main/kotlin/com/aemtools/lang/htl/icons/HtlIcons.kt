package com.aemtools.lang.htl.icons

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
object HtlIcons {

  val HTL_FILE_ICON = icon("htl.png")

  val DATA_SLY_LIST_ICON: Icon = AllIcons.Nodes.Variable

  val DATA_SLY_REPEAT_ICON: Icon = AllIcons.Nodes.Variable

  val TEMPLATE_PARAMETER_ICON: Icon = AllIcons.Nodes.Parameter

  val LIST_HELPER_ICON: Icon = AllIcons.Nodes.Variable
  val REPEAT_HELPER_ICON: Icon = AllIcons.Nodes.Variable

  val SLY_USE_VARIABLE_ICON: Icon = AllIcons.Nodes.Variable
  val SLY_TEST_VARIABLE_ICON: Icon = AllIcons.Nodes.Variable

  val AEM_COMPONENT: Icon = icon("aem-component.png")

  val HTL_ROOT: Icon = icon("/root/htlRoot.png")

  private fun icon(name: String) = IconLoader.getIcon("/icons/$name", HtlIcons::class.java)

}
