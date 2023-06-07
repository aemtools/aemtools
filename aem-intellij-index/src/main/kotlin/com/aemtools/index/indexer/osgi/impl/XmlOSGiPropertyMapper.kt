package com.aemtools.index.indexer.osgi.impl

import com.aemtools.index.indexer.osgi.OSGiPropertyMapper
import com.intellij.psi.xml.XmlAttribute

object XmlOSGiPropertyMapper : OSGiPropertyMapper<XmlAttribute> {
  override fun map(psiElement: XmlAttribute): Pair<String, String?> =
      psiElement.name to psiElement.value
}
