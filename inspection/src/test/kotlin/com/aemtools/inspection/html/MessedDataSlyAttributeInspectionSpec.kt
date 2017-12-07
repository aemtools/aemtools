package com.aemtools.inspection.html

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Troynikov
 */
object MessedDataSlyAttributeInspectionSpec : Spek({

  given("the inspection") {

    val tested = MessedDataSlyAttributeInspection()

    on("style check") {
      it("should have correct group display name") {
        assertThat(tested.groupDisplayName)
            .isEqualTo("HTL")
      }

      it("should have correct display name") {
        assertThat(tested.displayName)
            .isEqualTo("data-sly-attribute with prohibited attributes")
      }

      it("should have correct static description") {
        assertThat(tested.staticDescription)
            .isEqualTo("""
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>""".trimIndent())
      }
    }

  }

})
