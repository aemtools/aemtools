package com.aemtools.analysis.htl.callchain.typedescriptor.base

/**
 * Represents type which may be accessed like a map e.g.:
 *
 * ```
 * ${mapType[key]}
 * ```
 *
 * @author Dmytro Troynikov
 */
interface MapTypeDescriptor : TypeDescriptor {

  /**
   * The type of key.
   */
  fun keyType(): TypeDescriptor

  /**
   * The type of value.
   */
  fun valueType(): TypeDescriptor
}
