package com.aemtools.completion.htl.model.declaration

/**
 * The enumeration of declaration types.
 *
 * @author Dmytro Troynikov
 */
enum class DeclarationType {
  /**
   * The ordinary variable (spawned by `data-sly-use`, `data-sly-test`)
   */
  VARIABLE,
  /**
   * Iterable variable (`data-sly-list`, `data-sly-repeat`)
   */
  ITERABLE
}
