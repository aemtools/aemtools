package com.aemtools.lang.java.linemarker

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin
import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.lang.java.JavaSearch
import com.aemtools.util.isOSGiService

/**
 * @author Dmytro_Troynikov
 */
class OSGiConfigLineMarkerTest : BaseLightTest(),
        OSGiConfigFixtureMixin {

    fun testFelixService() = fileCase {
        addClass("MyService.java", """
            package com.test;

            import $FELIX_SERVICE_ANNOTATION;

            @Service
            public class MyService {}
        """)

        addClass("org/apache/felix/scr/annotations/Service.java", """
            package org.apache.felix.scr.annotations;

            public @interface Service {}
        """)
        addXml("/config/com.test.MyService.xml", emptyOSGiConfig())

        verify {
            val psiClass = JavaSearch.findClass("com.test.MyService", project)
                    ?: throw AssertionError("Unable to find fixture class!")

            assertTrue("The class should be determined as OSGi service", psiClass.isOSGiService())
        }
    }

}