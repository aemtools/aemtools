package com.aemtools.index.model.dialog.parameter

/**
 * Touch UI parameter declaration model.
 *
 * @property slingResourceType Sling resource type of underlying parameter (`sling:resourceType`).
 *
 * @author Dmytro Troynikov
 */
data class TouchUIDialogParameterDeclaration(
    val slingResourceType: String,
    override val name: String
) : BaseParameterDeclaration() {

  companion object {
    @JvmStatic
    val serialVersionUID: Long = 1L
  }

  override val tailText: String
    get() = slingResourceType

}
