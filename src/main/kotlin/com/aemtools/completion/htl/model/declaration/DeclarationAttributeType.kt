package com.aemtools.completion.htl.model.declaration

/**
 * @author Dmytro Troynikov
 */
enum class DeclarationAttributeType {
  /**
   * Variable spawned by `data-sly-use`.
   */
  DATA_SLY_USE,
  /**
   * Variable spawned by `data-sly-test`.
   */
  DATA_SLY_TEST,
  /**
   * Variable spawned by `data-sly-template`.
   */
  DATA_SLY_TEMPLATE,
  /**
   * Variable spawned by `data-sly-template's` parameter.
   */
  DATA_SLY_TEMPLATE_PARAMETER,
  /**
   * Variable spawned by `data-sly-list`.
   */
  DATA_SLY_LIST,
  /**
   * Variable spawned by `data-sly-repeat`.
   */
  DATA_SLY_REPEAT,
  /**
   * Helper variable spawned by `data-sly-repeat`.
   */
  REPEAT_HELPER,
  /**
   * Helper variable spawned by `data-sly-list`.
   */
  LIST_HELPER
}
