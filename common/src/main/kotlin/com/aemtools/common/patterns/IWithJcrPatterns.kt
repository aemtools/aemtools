package com.aemtools.common.patterns

import com.intellij.patterns.XmlPatterns

/**
 * @author Dmytro Primshyts
 */
interface IWithJcrPatterns {

  /**
   * Create matcher for `jcr:root` tag.
   */
  fun jcrRootTag() = XmlPatterns.xmlTag().withName("jcr:root")

  /**
   * Create matcher for `jcr:primaryType` attribute.
   */
  fun jcrPrimaryTypeAttribute() = XmlPatterns.xmlAttribute().withName("jcr:primaryType")


}
