package com.aemtools.index.model.dialog.parameter

/**
 * Classic dialog parameter declaration model.
 *
 * @property xtype The xtype type of underlying parameter.
 *
 * @author Dmytro Troynikov
 */
data class ClassicDialogParameterDeclaration(
    val xtype: String,
    override val name: String
) : BaseParameterDeclaration() {

  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }

  override val tailText: String
    get() = xtype

}
