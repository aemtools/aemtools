package com.aemtools.lang.htl.editor.comments

import com.intellij.lang.xml.XmlCommenter

/**
 * @author Dmytro Primshyts
 */
class HtlCommenter : XmlCommenter() {
  override fun getBlockCommentPrefix(): String = "<!--/* "

  override fun getBlockCommentSuffix(): String = " */-->"
}
