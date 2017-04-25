package com.aemtools.index.search

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin

/**
 * @author Dmytro Troynikov
 */
class OSGiConfigSearchTest : BaseLightTest(),
        OSGiConfigFixtureMixin {

    fun testBasicSearch() = fileCase {
        addXml("/config/com.test.Service.xml", emptyOSGiConfig())

        verify {
             OSGiConfigSearch.findConfigsForClass("com.test.Service", project)
                    .firstOrNull()
                ?: throw AssertionError("Unable to find configuration")
        }
    }

    fun testSearchForOSGiServiceFactory() = fileCase {
        addXml("/config/com.test.Service-first.xml", emptyOSGiConfig())
        addXml("/config/com.test.Service-second.xml", emptyOSGiConfig())

        verify {
            val configs = OSGiConfigSearch.findConfigsForClass("com.test.Service", project)

            // todo add assertion
        }
    }
}