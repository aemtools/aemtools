package com.aemtools.lang.clientlib.editor

import com.intellij.lang.Commenter

/**
 * @author Dmytro_Troynikov
 */
class CdCommenter : Commenter {
  override fun getCommentedBlockCommentPrefix(): String? = null

  override fun getCommentedBlockCommentSuffix(): String? = null

  override fun getBlockCommentPrefix(): String? = null

  override fun getBlockCommentSuffix(): String? = null

  override fun getLineCommentPrefix(): String? = "# "
}
