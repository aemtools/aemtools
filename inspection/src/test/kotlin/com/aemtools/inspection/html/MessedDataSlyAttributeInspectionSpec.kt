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
      it ("should have correct group display name") {
        assertThat(tested.groupDisplayName)
            .isEqualTo("HTL")
      }
    }

  }

})
