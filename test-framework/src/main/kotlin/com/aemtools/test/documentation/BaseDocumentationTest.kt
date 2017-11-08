package com.aemtools.test.documentation

import com.aemtools.test.base.BaseLightTest
import com.aemtools.test.documentation.model.DocTestFixture
import com.aemtools.test.documentation.model.IDocTestFixture
import com.intellij.lang.documentation.AbstractDocumentationProvider

/**
 * @author Dmytro Troynikov
 */
abstract class BaseDocumentationTest(
    val tested: AbstractDocumentationProvider,
    withAemUberJar: Boolean = false
) : BaseLightTest(withAemUberJar) {

  fun docCase(case: IDocTestFixture.() -> Unit) {
    val fixture = DocTestFixture(tested, myFixture)

    case.invoke(fixture)

    fixture.init()

    fixture.test()
  }

}
