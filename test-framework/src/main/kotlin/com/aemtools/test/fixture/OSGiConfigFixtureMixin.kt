package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Dmytro Primshyts
 */
interface OSGiConfigFixtureMixin {

  /**
   * Create empty OSGi config string.
   *
   * @return empty OSGi config
   */
  fun emptyOSGiConfig(): String = "".wrapInOSGiConfig()

  /**
   * Create OSGi configuration file.
   *
   * @param name the name of file
   * @param content content of file
   * @receiver [ITestFixture]
   */
  fun ITestFixture.osgiConfig(name: String, content: String) = addXml(name, content.wrapInOSGiConfig())

  /**
   * Wrap current String into OSGi config therefore,
   * the String should contain OSGi parameters in form of
   *
   * _parameter="value"_
   *
   * @receiver [String]
   * @return current string wrapped into OSGi config tag
   */
  fun String.wrapInOSGiConfig(): String = """
        <jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
            jcr:primaryType="sling:OsgiConfig"
            $this
            />
    """

  /**
   * Add list of empty OSGi config files to current fixture.
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addEmptyOSGiConfigs(vararg names: String) = names.forEach {
    this.addXml(it, emptyOSGiConfig())
  }

}
