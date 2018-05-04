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

  /**
   * Create matcher for `cq:dialogMode` attribute.
   */
  fun cqDialogModeAttribute() = XmlPatterns.xmlAttribute().withName("cq:dialogMode")

  /**
   * Create matcher for `cq:inherit` attribute.
   */
  fun cqInheritAttribute() = XmlPatterns.xmlAttribute().withName("cq:inherit")

  /**
   * Create matcher for `cq:layout` attribute.
   */
  fun cqLayoutAttribute() = XmlPatterns.xmlAttribute().withName("cq:layout")

}
