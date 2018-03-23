package com.aemtools.test.util

import com.intellij.openapi.project.Project
import org.mockito.Mockito
import org.mockito.Mockito.`when`

/**
 * Create mock object of given class or interface.
 *
 * @param TYPE classToMock
 * @return mock object
 */
inline fun <reified TYPE> mock(): TYPE = Mockito.mock(TYPE::class.java)

/**
 * Mock given service as component.
 *
 * @param project the project to attach the component to
 * @param service service that should be attached
 */
inline fun <reified SERVICE> mockComponent(project: Project, service: SERVICE) {
  `when`(project.getComponent(SERVICE::class.java))
      .thenReturn(service)
}
