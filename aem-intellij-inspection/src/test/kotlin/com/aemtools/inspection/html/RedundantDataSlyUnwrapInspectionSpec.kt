package com.aemtools.inspection.html

import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * Specification for [RedundantDataSlyUnwrapInspection].
 * @author Dmytro Primshyts
 */
object RedundantDataSlyUnwrapInspectionSpec : ShouldSpec({
  val tested = RedundantDataSlyUnwrapInspection()

  context("style check") {
    should("have correct group display name") {
      assertThat(tested.groupDisplayName)
        .isEqualTo("HTL")
    }

    should("have correct display name") {
      assertThat(tested.displayName)
        .isEqualTo("data-sly-unwrap is redundant inside sly tag")
    }

    should("have correct static description") {
      assertThat(tested.staticDescription)
        .isEqualTo(
          """
<html>
<body>
This inspection verifies that <i>data-sly-unwrap</i> is
<b>not</b> used inside of <i>sly</i> tag
</body>
</html>
          """.trimIndent()
        )

    }
  }
})
