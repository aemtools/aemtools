package com.aemtools.index.model.dialog

import com.aemtools.index.model.dialog.parameter.ClassicDialogParameterDeclaration

/**
 * Contains classic dialog information.
 *
 * @property fullPath Full path to component's classic dialog file (`dialog.xml`).
 *
 * @author Dmytro Troynikov
 */
data class AemComponentClassicDialogDefinition(
    override val fullPath: String,
    override val resourceType: String,
    override val myParameters: List<ClassicDialogParameterDeclaration>
) : AemComponentDialogBase<ClassicDialogParameterDeclaration>() {

  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }

}
