package com.aemtools.service.repository

/**
 * @author Dmytro Primshyts
 */
object const {

  private val DOCUMENTATION_DIR = "documentation"

  /**
   * File names.
   */
  object file {

    val WIDGET_DOCUMENTATION = "$DOCUMENTATION_DIR/documentation.json"

    val CQ_EDIT_CONFIG = "$DOCUMENTATION_DIR/cq-edit-config.json"

    val REP_POLICY = "$DOCUMENTATION_DIR/rep-policy.json"

    val CONTEXT_OBJECTS = "$DOCUMENTATION_DIR/context-objects.json"

    val SIGHTLY_ATTRIBUTES_DOCUMENTATION_DIRECTORY = "$DOCUMENTATION_DIR/htl-attributes"

    val HTL_OPTIONS_DIRECTORY = "$DOCUMENTATION_DIR/htl-options"

    val HTL_CONTEXT_VALUES = "$DOCUMENTATION_DIR/htl-context-values.json"

    val CQ_COMPONENT_VALUES = "$DOCUMENTATION_DIR/cq-component.json"
  }

}
