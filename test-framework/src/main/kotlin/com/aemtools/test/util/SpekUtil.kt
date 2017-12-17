package com.aemtools.test.util

import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.jetbrains.spek.api.lifecycle.LifecycleAware

/**
 * Memoized variable. By default, creation of the variable is delegated to mockito.
 * [CachingMode] is set to [CachingMode.TEST].
 *
 * Example usage:
 * ```kotlin
 * val xmlAttributePointer: SmartPsiElementPointer<XmlAttribute>> by memo()
 * ```
 *
 * @param initializer factory function
 * @return lifecycle aware instance
 */
inline fun <reified MOCK> SpecBody.memo(crossinline initializer: () -> MOCK = {
  com.aemtools.test.util.mock<MOCK>()
}): LifecycleAware<MOCK> {
  return memoized(CachingMode.TEST) {
    initializer()
  }
}
