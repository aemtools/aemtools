package com.aemtools.test.fixture

import com.aemtools.test.base.model.fixture.ITestFixture

/**
 * @author Dmytro Primshyts
 */
interface OSGiConfigFixtureMixin {

  /**
   * Create empty XML OSGi config string.
   *
   * @return empty XML OSGi config
   */
  fun emptyXmlOSGiConfig(): String = "".wrapInOSGiConfig()

  /**
   * Create empty JSON OSGi config string.
   *
   * @return empty JSON OSGi config
   */
  fun emptyJsonOSGiConfig(): String = "{}"

  /**
   * Create OSGi configuration file.
   *
   * @param name the name of file
   * @param content content of file
   * @receiver [ITestFixture]
   */
  fun ITestFixture.osgiConfig(name: String, content: String) {
    if (name.endsWith(".xml")) {
      addXml(name, content.wrapInOSGiConfig())
    } else if (name.endsWith(".json")) {
      addJson(name, content.wrapInJsonOSGiConfig())
    }
  }

  /**
   * Create OSGi configuration file.
   *
   * @param name the name of file
   * @param content content of file
   * @receiver [ITestFixture]
   */
  fun ITestFixture.osgiConfig(name: String, parameters: Map<String, String?>) {
    if (name.endsWith(".xml")) {
      val content = parameters.map { "${it.key}=\"${it.value}\"" }.joinToString("\n")
      addXml(name, content.wrapInOSGiConfig())
    } else if (name.endsWith(".json")) {
      val content = parameters.map { "\"${it.key}\":\"${it.value}\"" }.joinToString(",\n")
      addJson(name, content.wrapInJsonOSGiConfig())
    }
  }

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
   * Wrap current String into JSON OSGi config therefore,
   * the String should contain OSGi parameters in form of
   *
   * _"property"="value"_
   *
   * @receiver [String]
   * @return current string wrapped into JSON OSGi config
   */
  fun String.wrapInJsonOSGiConfig(): String = """{$this}"""

  /**
   * Add list of empty OSGi config files to current fixture.
   * @receiver [ITestFixture]
   */
  fun ITestFixture.addEmptyOSGiConfigs(vararg names: String) = names.forEach {
    this.osgiConfig(it, "")
  }

}
