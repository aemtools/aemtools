package com.aemtools.test.base.model.fixture

import com.aemtools.test.base.model.assertion.IAssertionContext
import org.intellij.lang.annotations.Language

/**
 * @author Dmytro Primshyts
 */
interface ITestFixture {

  /**
   * Add html file to the fixture.
   * @param name name of the file
   * @param text the body of html file
   */
  fun addHtml(name: String,
              @Language("HTML") text: String)

  /**
   * Add java class to the fixture.
   * @param text java class
   */
  fun addClass(name: String,
               @Language("Java") text: String)

  /**
   * Add XML file to the fixture.
   * @param name the file name (path may be used)
   * @param text the file content
   */
  fun addXml(name: String, @Language("XML") text: String)

  /**
   * Add JSON file to the fixture.
   * @param name the file name (path may be used)
   * @param text the file content
   */
  fun addJson(name: String, @Language("JSON") text: String)

  /**
   * Add arbitrary file to the fixture.
   * @param name the name of the file (path may be used)
   * @param text the content of the file
   */
  fun addFile(name: String, text: String)

  /**
   * Pass lambda with case specific assertions.
   *
   * @param verification lambda with custom assertions
   * @receiver [IAssertionContext]
   */
  fun verify(verification: IAssertionContext.() -> Unit)

}
