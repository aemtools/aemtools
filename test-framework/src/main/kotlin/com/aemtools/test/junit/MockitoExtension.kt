package com.aemtools.test.junit

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.lang.reflect.Parameter

class MockitoExtension : TestInstancePostProcessor, ParameterResolver {

  override fun postProcessTestInstance(testInstance: Any,
                                       context: ExtensionContext) {
    MockitoAnnotations.initMocks(testInstance)
  }

  override fun supportsParameter(parameterContext: ParameterContext,
                                 extensionContext: ExtensionContext): Boolean {
    return parameterContext.getParameter().isAnnotationPresent(Mock::class.java)
  }

  override fun resolveParameter(parameterContext: ParameterContext,
                                extensionContext: ExtensionContext): Any {
    return getMock(parameterContext.getParameter(), extensionContext)
  }

  private fun getMock(
      parameter: Parameter, extensionContext: ExtensionContext): Any {

    val mockType = parameter.getType()
    val mocks = extensionContext.getStore(ExtensionContext.Namespace.create(
        MockitoExtension::class.java, mockType))
    val mockName = getMockName(parameter)

    return if (mockName != null) {
      mocks.getOrComputeIfAbsent(
          mockName, { key -> mock(mockType, mockName) })
    } else {
      mocks.getOrComputeIfAbsent(
          mockType.getCanonicalName(), { key -> mock(mockType) })
    }
  }

  private fun getMockName(parameter: Parameter): String? {
    val explicitMockName = parameter.getAnnotation(Mock::class.java)
        .name.trim()
    if (!explicitMockName.isEmpty()) {
      return explicitMockName
    } else if (parameter.isNamePresent()) {
      return parameter.getName()
    }
    return null
  }
}
