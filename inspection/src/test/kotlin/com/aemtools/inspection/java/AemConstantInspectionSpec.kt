package com.aemtools.inspection.java

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for [AemConstantInspection].
 *
 * @author Dmytro Troynikov
 */
object AemConstantInspectionSpec : Spek({
  val tested = AemConstantInspection()

  on("style check") {
    it("should have correct group display name") {
      assertThat(tested.groupDisplayName)
          .isEqualTo("AEM")
    }

    it("should have correct display name") {
      assertThat(tested.displayName)
          .isEqualTo("Hardcoded AEM specific literal")
    }

    it("should have correct static description") {
      assertThat(tested.staticDescription)
          .isEqualTo("""
<html>
<body>
This inspection verifies that predefined AEM constants are used instead of
hardcode.
</body>
</html>""".trimIndent())
    }
  }

})
