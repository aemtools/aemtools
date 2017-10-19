package com.aemtools.analysis.htl.callchain.typedescriptor.base

/**
 * @author Dmytro Troynikov
 */
abstract class BaseTypeDescriptor : TypeDescriptor {

  override fun isArray(): Boolean = false
  override fun isIterable(): Boolean = false
  override fun isMap(): Boolean = false

}
