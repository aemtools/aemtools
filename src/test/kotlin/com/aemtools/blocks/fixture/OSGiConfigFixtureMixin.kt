package com.aemtools.blocks.fixture

/**
 * @author Dmytro_Troynikov
 */
interface OSGiConfigFixtureMixin {

    /**
     * Create empty OSGi config string.
     *
     * @return empty OSGi config
     */
    fun emptyOSGiConfig(): String = "".wrapInOSGiConfig()

    /**
     * Wrap current String into OSGi config therefore,
     * the String should contain OSGi parameters in form of
     *
     *
     * _parameter="value"_
     *
     * @return current string wrapped into OSGi config tag
     */
    fun String.wrapInOSGiConfig(): String = """
        <jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
            jcr:primaryType="sling:OsgiConfig"
            $this
            />
    """

}