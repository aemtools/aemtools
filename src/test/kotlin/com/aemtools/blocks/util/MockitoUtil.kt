package com.aemtools.blocks.util

import org.mockito.Mockito

/**
 * Create mock object of given class or interface.
 *
 * @param TYPE classToMock
 * @return mock object
 */
inline fun <reified TYPE> mock(): TYPE = Mockito.mock(TYPE::class.java)
