package com.aemtools.completion.htl.model.declaration

import com.aemtools.test.util.mock
import com.intellij.psi.xml.XmlAttribute
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * Test for [HtlVariableDeclaration] & [HtlVariableDeclaration.Companion].
 *
 * @author Dmytro Troynikov
 */
class HtlVariableDeclarationTest {

  @Test
  fun `create with non htl attribute`() {
    assertThat(HtlVariableDeclaration.create(attribute("unknown")))
        .isEmpty()
  }

  @Test
  fun `create data-sly-use`() = builderTest(
      attribute("data-sly-use.bean", "com.test.Bean"),
      ExpectedVariable(
          HtlUseVariableDeclaration::class.java,
          "bean",
          DeclarationAttributeType.DATA_SLY_USE,
          DeclarationType.VARIABLE
      )
  )

  @Test
  fun `create data-sly-test`() = builderTest(
      attribute("data-sly-test.condition", ""),
      ExpectedVariable(
          HtlVariableDeclaration::class.java,
          "condition",
          DeclarationAttributeType.DATA_SLY_TEST,
          DeclarationType.VARIABLE
      )
  )

  @Test
  fun `create data-sly-list with default names`() = builderTest(
      attribute("data-sly-list"),
      ExpectedVariable(
          HtlVariableDeclaration::class.java,
          "item",
           DeclarationAttributeType.DATA_SLY_LIST,
          DeclarationType.ITERABLE
      ),
      ExpectedVariable(
          HtlListHelperDeclaration::class.java,
          "itemList",
          DeclarationAttributeType.LIST_HELPER,
          DeclarationType.VARIABLE
      )
  )

  @Test
  fun `create data-sly-list with custom names`() = builderTest(
      attribute("data-sly-list.myName"),
      ExpectedVariable(
          HtlVariableDeclaration::class.java,
          "myName",
          DeclarationAttributeType.DATA_SLY_LIST,
          DeclarationType.ITERABLE
      ),
      ExpectedVariable(
          HtlListHelperDeclaration::class.java,
          "myNameList",
          DeclarationAttributeType.LIST_HELPER,
          DeclarationType.VARIABLE
      )
  )

  @Test
  fun `create data-sly-repeat with default names`() = builderTest(
      attribute("data-sly-repeat"),
      ExpectedVariable(
          HtlVariableDeclaration::class.java,
          "item",
          DeclarationAttributeType.DATA_SLY_REPEAT,
          DeclarationType.ITERABLE
      ),
      ExpectedVariable(
          HtlListHelperDeclaration::class.java,
          "itemList",
          DeclarationAttributeType.REPEAT_HELPER,
          DeclarationType.VARIABLE
      )
  )

  @Test
  fun `create data-sly-repeat with custom names`() = builderTest(
      attribute("data-sly-repeat.myName"),
      ExpectedVariable(
          HtlVariableDeclaration::class.java,
          "myName",
          DeclarationAttributeType.DATA_SLY_REPEAT,
          DeclarationType.ITERABLE
      ),
      ExpectedVariable(
          HtlListHelperDeclaration::class.java,
          "myNameList",
          DeclarationAttributeType.REPEAT_HELPER,
          DeclarationType.VARIABLE
      )
  )

  private fun builderTest(input: XmlAttribute, vararg output: ExpectedVariable) {
    val result = HtlVariableDeclaration.create(input)

    if (result.size != output.size) {
      throw AssertionError("Unexpected result size: ${result.size}, expected: ${output.size}")
    }

    result.forEachIndexed { index, variable ->
      val expectation = output[index]
      assertThat(variable)
          .isInstanceOf(expectation.clazz)

      assertThat(variable.variableName)
          .isEqualTo(expectation.variableName)

      assertThat(variable.type)
          .isEqualTo(expectation.type)

      assertThat(variable.attributeType)
          .isEqualTo(expectation.attributeType)
    }

  }

  data class ExpectedVariable(
      val clazz: Class<*>,
      val variableName: String,
      val attributeType: DeclarationAttributeType,
      val type: DeclarationType
  )

  private fun attribute(name: String, value: String = "") : XmlAttribute
    = mock<XmlAttribute>().apply {
    `when`(this.name).thenReturn(name)
    `when`(this.value).thenReturn(value)
  }

}
