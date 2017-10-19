package com.aemtools.index.model.dialog

import com.aemtools.index.model.dialog.parameter.TouchUIDialogParameterDeclaration

/**
 * Contains aem component definition information
 * from touch ui dialog.
 *
 * @author Dmytro Troynikov
 */
data class AemComponentTouchUIDialogDefinition(
    override val fullPath: String,
    override val resourceType: String,
    override val myParameters: List<TouchUIDialogParameterDeclaration>
) : AemComponentDialogBase<TouchUIDialogParameterDeclaration>() {

  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }

}

