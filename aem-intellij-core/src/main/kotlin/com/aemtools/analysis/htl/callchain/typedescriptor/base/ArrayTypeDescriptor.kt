package com.aemtools.analysis.htl.callchain.typedescriptor.base

/**
 * @author Dmytro Primshyts
 */
interface ArrayTypeDescriptor : TypeDescriptor {

  /**
   * Getter for array type descriptor.
   *
   * @return type descriptor for array
   */
  fun arrayType(): TypeDescriptor

}
