package com.aemtools.refactoring.htl.rename

import com.aemtools.completion.util.htlAttributeName
import com.aemtools.completion.util.htlVariableName
import com.aemtools.completion.util.isHtlDeclarationAttribute
import com.aemtools.reference.common.reference.HtlPropertyAccessReference
import com.aemtools.reference.htl.reference.HtlDeclarationReference
import com.aemtools.reference.htl.reference.HtlListHelperReference
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenamePsiElementProcessor
import com.intellij.usageView.UsageInfo
import java.awt.GridBagConstraints
import javax.swing.JCheckBox
import javax.swing.JPanel

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeRenameProcessor : RenamePsiElementProcessor() {

  override fun canProcessElement(element: PsiElement): Boolean {
    return element is XmlAttribute
        && element.isHtlDeclarationAttribute()
  }

  override fun renameElement(
      element: PsiElement?,
      newName: String?,
      usages: Array<out UsageInfo>?,
      listener: RefactoringElementListener?) {
    if (element == null || newName == null) {
      return
    }

    val attribute = element as? XmlAttribute
        ?: return

    val htlAttributeName = attribute.htlAttributeName() ?: return

    val newAttributeName = "$htlAttributeName.$newName"
    attribute.setName(newAttributeName)

    val htlDeclarationUsages: ArrayList<UsageInfo> = ArrayList()
    val htlListHelperUsages: ArrayList<UsageInfo> = ArrayList()
    val propertyAccessUsages: ArrayList<UsageInfo> = ArrayList()
    usages?.filterTo(htlDeclarationUsages, { it.reference is HtlDeclarationReference })
    usages?.filterTo(htlListHelperUsages, { it.reference is HtlListHelperReference })
    usages?.filterTo(propertyAccessUsages, { it.reference is HtlPropertyAccessReference })

    htlListHelperUsages.forEach {
      it.reference?.handleElementRename("${newName}List")
    }

    htlDeclarationUsages.forEach {
      it.reference?.handleElementRename(newName)
    }

    propertyAccessUsages.forEach {
      it.reference?.handleElementRename(newName)
    }

    listener?.elementRenamed(attribute)
  }

  override fun isInplaceRenameSupported(): Boolean {
    return true
  }

  override fun createRenameDialog(project: Project?,
                                  element: PsiElement?,
                                  nameSuggestionContext: PsiElement?,
                                  editor: Editor?): RenameDialog {
    if (project == null || element == null) {
      throw IllegalArgumentException("project and element should be not null")
    }

    return HtlAttributeRenameDialog(project,
        element,
        nameSuggestionContext,
        editor)
  }

}

/**
 * Htl attribute rename dialog.
 */
class HtlAttributeRenameDialog(project: Project,
                               element: PsiElement,
                               context: PsiElement?,
                               editor: Editor?)
  : RenameDialog(project, element, context, editor) {

  override fun hasPreviewButton(): Boolean = false

  override fun isToSearchForTextOccurrencesForRename(): Boolean = false
  override fun isToSearchInCommentsForRename(): Boolean = false
  override fun createCheckboxes(panel: JPanel?, gbConstraints: GridBagConstraints?) {
    super.createCheckboxes(panel, gbConstraints)
    // hide checkboxes
    panel?.let {
      it.components.filter {
        it is JCheckBox
      }.forEach {
        it.isVisible = false
      }
    }
  }

  override fun getSuggestedNames(): Array<String> {
    val attribute = psiElement as? XmlAttribute
    val name = attribute?.htlVariableName()
    return arrayOf(name ?: "")
  }

}
