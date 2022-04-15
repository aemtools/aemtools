package com.aemtools.codeinsight.htl.model

/**
 * Types of `data-sly-use` declaration.
 * @author Dmytro Primshyts
 */
enum class UseType {
  /**
   * Implementor of `Use` interface or a `Sling` model.
   */
  BEAN,
  /**
   * Javascript file.
   */
  JAVASCRIPT,
  /**
   * The HTL file.
   */
  HTL,
  /**
   * Unable to resolve data-sly-use.
   */
  UNKNOWN
}
