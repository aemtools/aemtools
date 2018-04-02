package com.aemtools.common.util

import com.aemtools.test.psi.mockAttribute
import com.intellij.psi.xml.XmlTag
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Dmytro Primshyts
 */
object JcrUtilSpek : Spek({

  describe("XmlTag.jcrProperty") {

    group("with boolean") {
      data class BooleanTestData(
          val input: String,
          val expected: Boolean? = null
      )

      val testData = listOf(
          BooleanTestData(
              "{Boolean}true", true
          ),
          BooleanTestData(
              "{Boolean}false", false
          ),
          BooleanTestData(
              "{Boolean}wrong", null
          ),
          BooleanTestData(
              "true", true
          ),
          BooleanTestData(
              "false", false
          ),
          BooleanTestData(
              "wrong", null
          )
      )
      testData.forEach { data ->
        it("should evaluate ${data.input} to ${data.expected}") {
          val tag = mock<XmlTag>().apply {
            mockAttribute("test", data.input)
          }

          assertThat(tag.jcrProperty<Boolean>("test")).apply {
            when (data.expected) {
              true -> isTrue()
              false -> isFalse()
              else -> isNull()
            }
          }
        }
      }
    }

  }

})
