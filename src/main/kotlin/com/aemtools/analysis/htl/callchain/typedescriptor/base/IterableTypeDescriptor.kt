package com.aemtools.analysis.htl.callchain.typedescriptor.base

/**
 * Represents type which may be used inside of `data-sly-list` or
 * `data-sly-repeat`.
 *
 * @author Dmytro Troynikov
 */
interface IterableTypeDescriptor : TypeDescriptor {

  /**
   * Getter for iterable type descriptor.
   *
   * @return type descriptor for iterable type
   */
  fun iterableType(): TypeDescriptor

}
