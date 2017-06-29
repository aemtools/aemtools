package com.aemtools.refactoring.htl.rename.inplace

import com.aemtools.completion.util.isHtlLocalDeclarationAttribute
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.refactoring.rename.PsiElementRenameHandler
import com.intellij.refactoring.rename.RenameHandler
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer
import com.intellij.refactoring.util.CommonRefactoringUtil
import java.util.ArrayList

/**
 * @author Dmytro Troynikov
 */
class HtlDeclarationAttributeInplaceRenameHandler : RenameHandler {

    override fun isRenaming(dataContext: DataContext?): Boolean {
        return isAvailableOnDataContext(dataContext)
    }

    override fun isAvailableOnDataContext(dataContext: DataContext?): Boolean {
        if (dataContext == null) {
            return false
        }

        val element = PsiElementRenameHandler.getElement(dataContext)
                as? XmlAttribute
                ?: return false

        return element.isHtlLocalDeclarationAttribute()
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
        if (dataContext == null || editor == null || file == null) {
            return
        }

        val attribute = PsiElementRenameHandler.getElement(dataContext) as? XmlAttribute
                ?: return

        val renamer = HtlVariableInplaceRenamer(attribute, editor)
        renamer.performInplaceRename()
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class HtlVariableInplaceRenamer(val attribute: XmlAttribute,
                                val editor: Editor)
    : MemberInplaceRenamer(XmlAttributeForInplaceRename(attribute), attribute, editor) {

    override fun performInplaceRefactoring(nameSuggestions: LinkedHashSet<String>?): Boolean {
        myNameSuggestions = nameSuggestions
        if (InjectedLanguageUtil.isInInjectedLanguagePrefixSuffix(myElementToRename)) {
            return false
        }

        val fileViewProvider = myElementToRename.containingFile.viewProvider
        val file = getTopLevelVirtualFile(fileViewProvider)

        val referencesSearchScope = getReferencesSearchScope(file)

        val refs = collectRefs(referencesSearchScope)

        addReferenceAtCaret(refs)

        val scope = checkLocalScope() ?: return false // Should have valid local search scope for inplace rename

        val containingFile = scope.containingFile ?: return false // Should have valid local search scope for inplace rename
        //no need to process further when file is read-only
        if (!CommonRefactoringUtil.checkReadOnlyStatus(myProject, containingFile)) return true

        myEditor.putUserData(INPLACE_RENAMER, this)
        ourRenamersStack.push(this)

        val stringUsages = ArrayList<Pair<PsiElement, TextRange>>()
        collectAdditionalElementsToRename(stringUsages)
        return buildTemplateAndStart(refs, stringUsages, scope, containingFile)
    }

    override fun getNameIdentifier(): PsiElement {
        return FakeXmlAttributeNameIdentifier(attribute)
    }
}

class XmlAttributeForInplaceRename(
        val originalAttribute: XmlAttribute
) : XmlAttribute by originalAttribute, PsiNameIdentifierOwner {

    override fun getNameIdentifier() = FakeXmlAttributeNameIdentifier(originalAttribute)

}

class FakeXmlAttributeNameIdentifier(val attribute: XmlAttribute) : FakePsiElement() {
    override fun getParent(): PsiElement = attribute

    override fun getTextRange(): TextRange? {
        return TextRange(
                attribute.textRange.startOffset + attribute.name.indexOf(".") + 1,
                attribute.textRange.endOffset)
    }

}