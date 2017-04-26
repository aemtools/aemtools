package com.aemtools.lang.java.linemarker

import com.aemtools.blocks.base.BaseLightTest
import com.aemtools.blocks.fixture.OSGiConfigFixtureMixin
import com.aemtools.blocks.fixture.OSGiFelixAnnotationsMixin
import com.aemtools.constant.const.java.FELIX_SERVICE_ANNOTATION
import com.aemtools.lang.java.JavaSearch
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.psi.PsiElement
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * @author Dmytro_Troynikov
 */

class OSGiConfigLineMarkerTest : BaseLightTest(),
        OSGiConfigFixtureMixin,
        OSGiFelixAnnotationsMixin {

    var tested: OSGiConfigLineMarker = OSGiConfigLineMarker()

    @Rule var mockitoRule: MockitoRule = MockitoJUnit.rule()

    fun testFelixServiceShouldBeMarked() = fileCase {
        addFelixServiceAnnotation()
        addClass("MyService.java", """
            package com.test;

            import $FELIX_SERVICE_ANNOTATION;

            @Service
            public class MyService {}
        """)
        addEmptyOSGiConfigs("/config/com.test.MyService.xml")

        verify {
            val psiClass = JavaSearch.findClass("com.test.MyService", project)
                    ?: throw AssertionError("Unable to find fixture class!")
            val classIdentifier = psiClass.nameIdentifier
                    ?: throw AssertionError("Unable to get class identifier!")

            val marker = tested.getLineMarkerInfo(classIdentifier)

            assertNotNull("Marker should be created for given identifier", marker)
        }
    }

    fun testFastMarkerShouldReturnTheSameResultAsTheSlowOne() = fileCase {
        addFelixServiceAnnotation()
        addClass("MyService.java", """
            package com.test;

            import $FELIX_SERVICE_ANNOTATION;

            @Service
            public class MyService {}
        """)
        addEmptyOSGiConfigs("/config/com.test.MyService.xml")

        verify {
            val psiClass = JavaSearch.findClass("com.test.MyService", project)
                    ?: throw AssertionError("Unable to find fixture class!")
            val classIdentifier = psiClass.nameIdentifier
                    ?: throw AssertionError("Unable to get class identifier!")

            val marker = tested.getLineMarkerInfo(classIdentifier)

            assertNotNull("Marker should be created for given identifier", marker)

            val mutableCollection: ArrayList<LineMarkerInfo<PsiElement>> = ArrayList()

            tested.collectSlowLineMarkers(mutableListOf(*psiClass.children), mutableCollection)

            assertEquals("Single marker should be added", 1, mutableCollection.size)

            assertEquals("Markers should be equal", marker, mutableCollection.firstOrNull())

            assertEquals("Navigation handlers should be equal",
                    marker?.navigationHandler,
                    mutableCollection.firstOrNull()?.navigationHandler)
        }
    }

}