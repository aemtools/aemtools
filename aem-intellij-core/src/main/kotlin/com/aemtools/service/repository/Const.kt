package com.aemtools.service.repository

/**
 * @author Dmytro Primshyts
 */
object Const {

  private const val DOCUMENTATION_DIR = "documentation"

  /**
   * File names.
   */
  object File {

    const val WIDGET_DOCUMENTATION = "$DOCUMENTATION_DIR/documentation.json"

    val CQ_EDIT_CONFIG = "$DOCUMENTATION_DIR/cq-edit-config.json"

    const val REP_POLICY = "$DOCUMENTATION_DIR/rep-policy.json"

    const val CONTEXT_OBJECTS = "$DOCUMENTATION_DIR/context-objects.json"

    const val SIGHTLY_ATTRIBUTES_DOCUMENTATION_DIRECTORY = "$DOCUMENTATION_DIR/htl-attributes"

    const val HTL_OPTIONS_DIRECTORY = "$DOCUMENTATION_DIR/htl-options"

    const val HTL_CONTEXT_VALUES = "$DOCUMENTATION_DIR/htl-context-values.json"

    const val CQ_COMPONENT_VALUES = "$DOCUMENTATION_DIR/cq-component.json"
  }
}
