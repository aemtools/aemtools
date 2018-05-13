package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture


/**
 * Clientlibrary utilities mixin.
 *
 * @author Dmytro Primshyts
 */
interface ClientlibraryMixin {

  /**
   * Add clientlibrary definition to current [ITestFixture].
   *
   * @param path the path to definition file
   * @param channels the channels
   * @param categories the categories
   * @param dependencies the dependencies
   * @param embed the embed
   *
   * @receiver [ITestFixture]
   */
  fun ITestFixture.clientLibrary(
      path: String,
      channels: String = "",
      categories: String = "",
      dependencies: String = "",
      embed: String = ""
  ) {


    val body = buildString {

    }

  }

}
