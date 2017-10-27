package com.aemtools.settings

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for [HtlRootDirectories].
 *
 * @author Dmytro Troynikov
 */
class HtlRootDirectoriesTest {

  private val tested: HtlRootDirectories = HtlRootDirectories()

  @Test
  fun `addRoot should persist added folder`() {
    tested.addRoot("/test/path")

    assertThat(tested.directories)
        .size().isEqualTo(1)

    assertThat(tested.directories)
        .contains("/test/path")
  }

  @Test
  fun `removeRoot should remove given root`() {
    tested.addRoot("/test/path")
    tested.removeRoot("/test/path")

    assertThat(tested.directories)
        .isEmpty()
  }

  @Test
  fun `loadState should override existing roots`() {
    val newState = HtlRootDirectories()
    newState.addRoot("/new/root")

    tested.addRoot("/old/root")
    tested.loadState(newState)

    assertThat(tested.directories)
        .size().isEqualTo(1)

    assertThat(tested.directories)
        .contains("/new/root")
  }

  @Test
  fun `loadState with null argument should clear roots`() {
    tested.addRoot("/test/root")
    tested.loadState(null)

    assertThat(tested.directories)
        .isEmpty()
  }

  @Test
  fun `getState should return this`() {
    assertThat(tested.state)
        .isEqualTo(tested)
  }

}
