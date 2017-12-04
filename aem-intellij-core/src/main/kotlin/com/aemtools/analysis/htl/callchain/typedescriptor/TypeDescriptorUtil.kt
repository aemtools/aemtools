package com.aemtools.analysis.htl.callchain.typedescriptor

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor

/**
 * Check if current [TypeDescriptor] may be used as
 * `data-sly-list` or `data-sly-repeat` argument.
 *
 * @receiver [TypeDescriptor]
 * @return *true* if current type is iterable,
 * *false* otherwise
 */
fun TypeDescriptor.mayBeIteratedUpon(): Boolean
    = isArray() || isIterable() || isMap()
