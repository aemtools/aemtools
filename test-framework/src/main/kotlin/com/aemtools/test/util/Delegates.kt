package com.aemtools.test.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Require not null delegate demands from underlying supplier to
 * provide not null value.
 * If supplier returns __null__ [IllegalArgumentException] will be thrown.
 *
 * @author Dmytro Troynikov
 */
class RequireNotNull<out T>(private val supplier: () -> T?) {

  operator fun provideDelegate(thisRef: Nothing?, prop: KProperty<*>): ReadOnlyProperty<Nothing?, T> {
    val result = supplier.invoke()
    return object : ReadOnlyProperty<Nothing?, T> {
      override fun getValue(thisRef: Nothing?, property: KProperty<*>): T {
        if (result == null) {
          throw IllegalArgumentException("Supplier provided null for ${property.name}")
        }
        return result
      }
    }
  }
}

/**
 * Require not null delegate
 * @param supplier supplier of value
 * @return [RequireNotNull] delegate
 */
fun <T> notNull(supplier: () -> T?): RequireNotNull<T>
    = RequireNotNull(supplier)
