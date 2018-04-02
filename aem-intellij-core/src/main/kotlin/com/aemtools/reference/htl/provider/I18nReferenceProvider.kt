package com.aemtools.reference.htl.provider

import com.aemtools.common.util.findChildrenByType
import com.aemtools.common.util.findParentByType
import com.aemtools.index.HtlIndexFacade
import com.aemtools.index.model.LocalizationModel
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.mixin.HtlStringLiteralMixin
import com.intellij.icons.AllIcons
import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import javax.swing.Icon

/**
 * @author Dmytro Primshyts
 */
object I18nReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
    val htlString = element as? HtlStringLiteralMixin ?: return emptyArray()

    if (localizationMainString(element)) {
      val value = htlString.name

      val localizations = HtlIndexFacade
          .getLocalizationModelsForKey(htlString.project, value)

      val filtered = localizations.filter { it.key == value }
          .mapNotNull {
            val declaration = it.resolve(element.project)
            if (declaration != null) {
              it to declaration
            } else {
              null
            }
          }.toMap()

      if (filtered.isEmpty()) {
        return emptyArray()
      }

      return arrayOf(I18nReference(filtered, htlString))
    }

    return emptyArray()
  }

  private fun localizationMainString(position: PsiElement): Boolean {
    return position.findParentByType(HtlHtlEl::class.java)
        ?.findChildrenByType(PsiElement::class.java)
        ?.any { it.text == "i18n" }
        ?: false
  }

  private class I18nReference(
      val declarations: Map<LocalizationModel, XmlTag>,
      htlStringLiteralMixin: HtlStringLiteralMixin
  ) : PsiPolyVariantReferenceBase<HtlStringLiteralMixin>(
      htlStringLiteralMixin,
      TextRange.create(1, htlStringLiteralMixin.name.length + 1),
      true
  ) {
    override fun getVariants(): Array<Any> {
      return emptyArray()
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
      return declarations.map {
        I18nResolveResult(it.value, it.key)
      }.toTypedArray()
    }

  }

  class I18nResolveResult(val xmlTag: XmlTag,
                          val localizationModel: LocalizationModel) : PsiElementResolveResult(xmlTag) {
    override fun getElement(): PsiElement {
      return I18nNavigationWrapper(xmlTag, localizationModel)
    }

    override fun isValidResult(): Boolean = true
  }

  class I18nNavigationWrapper(val xmlTag: XmlTag,
                              val localizationModel: LocalizationModel) : NavigationItem,
      XmlTag by xmlTag {
    override fun navigate(requestFocus: Boolean) {
      val offset = xmlTag.textOffset
      val virtualFile = PsiUtilCore.getVirtualFile(xmlTag)
      if (virtualFile != null && virtualFile.isValid) {
        PsiNavigationSupport.getInstance()
            .createNavigatable(xmlTag.project,
                virtualFile,
                offset).navigate(requestFocus)
      }
    }

    override fun getPresentation(): ItemPresentation? {
      return object : ItemPresentation {
        override fun getLocationString(): String? {
          return localizationModel.message
        }

        override fun getIcon(unused: Boolean): Icon? {
          return AllIcons.Nodes.ResourceBundle
        }

        override fun getPresentableText(): String? {
          return localizationModel.language
        }
      }
    }

    override fun canNavigate(): Boolean = true

    override fun getName(): String = localizationModel.key

    override fun canNavigateToSource(): Boolean = true

  }

}
