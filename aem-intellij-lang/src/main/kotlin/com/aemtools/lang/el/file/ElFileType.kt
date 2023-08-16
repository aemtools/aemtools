package com.aemtools.lang.el.file

import com.aemtools.lang.el.ElLanguage
import com.intellij.ide.ui.ProductIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object ElFileType : LanguageFileType(ElLanguage) {

  override fun getIcon(): Icon = ProductIcons.getInstance().productIcon

  override fun getName(): String = "Expression Language"

  override fun getDefaultExtension(): String = "el"

  override fun getDescription(): String = "EL Test File"

}
