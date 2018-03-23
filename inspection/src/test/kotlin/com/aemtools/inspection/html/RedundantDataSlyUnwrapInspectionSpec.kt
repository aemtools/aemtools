package com.aemtools.inspection.html

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Specification for [RedundantDataSlyUnwrapInspection].
 * @author Dmytro Troynikov
 */
object RedundantDataSlyUnwrapInspectionSpec : Spek({
  val tested = RedundantDataSlyUnwrapInspection()

  on("style check") {
    it("should have correct group display name") {
      assertThat(tested.groupDisplayName)
          .isEqualTo("HTL")
    }

    it("should have correct display name") {
      assertThat(tested.displayName)
          .isEqualTo("data-sly-unwrap is redundant inside sly tag")
    }

    it("should have correct static description") {
      assertThat(tested.staticDescription)
          .isEqualTo("""
<html>
<body>
This inspection verifies that <i>data-sly-unwrap</i> is
<b>not</b> used inside of <i>sly</i> tag
</body>
</html>
          """.trimIndent())

    }
  }
})
