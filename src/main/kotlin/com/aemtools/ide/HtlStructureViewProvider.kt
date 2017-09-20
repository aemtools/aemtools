package com.aemtools.ide

import com.aemtools.completion.util.getHtmlFile
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.html.HtmlFileImpl

/**
 * @author Dmytro Troynikov
 */
class HtlStructureViewProvider : PsiStructureViewFactory {
  override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
    val htmlFile = psiFile.getHtmlFile() as? HtmlFileImpl
    var htmlStructureViewBuilder: StructureViewBuilder? = null
    if (htmlFile != null) {
      htmlStructureViewBuilder = LanguageStructureViewBuilder.INSTANCE.getStructureViewBuilder(htmlFile)
    }
    return object : TreeBasedStructureViewBuilder() {
      override fun createStructureViewModel(editor: Editor?): StructureViewModel {
        val _htmlStructureViewBuilder = htmlStructureViewBuilder
        if (_htmlStructureViewBuilder != null && htmlFile != null) {
          val fileEditor = FileEditorManager.getInstance(psiFile.project).getSelectedEditor(psiFile.virtualFile)
              as? FileEditor
          val viewBuilder = _htmlStructureViewBuilder.createStructureView(fileEditor, psiFile.project)
          return viewBuilder.treeModel
        }
        return StructureViewModelBase(psiFile,
            editor,
            HtlStructureViewElement(psiFile))
      }
    }

  }

}

/**
 * Htl structure view element.
 */
class HtlStructureViewElement(val file: PsiFile) : PsiTreeElementBase<PsiElement>(file) {
  override fun getPresentableText(): String? {
    return "HtlStructure: ${file.name}"
  }

  override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
    return mutableListOf()
  }
}
