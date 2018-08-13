package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * Clientlibrary utilities mixin.
 *
 * @author Dmytro Primshyts
 */
interface ClientlibraryMixin {


}


/**
 * Add clientlibrary definition to current [ITestFixture].
 *
 * @param fileName the name of client library definition file
 * @param categories the categories
 * @param channels the channels
 * @param dependencies the dependencies
 * @param embed the embed
 *
 * @receiver [ITestFixture]
 */
fun ITestFixture.clientLibrary(
    fileName: String,
    categories: List<String>,
    dependencies: List<String> = emptyList(),
    embed: List<String> = emptyList(),
    channels: List<String> = emptyList()
) {
  addXml(fileName, """
        <jcr:root jcr:primaryType="cq:ClientLibraryFolder"
             categories="$categories"
             dependencies="$dependencies"
             embed="$embed"
             channels="$channels"
    """.trimIndent())
}
