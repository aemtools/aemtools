package com.aemtools.lang.htl.psi

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.constant.const.DOLLAR
import com.aemtools.lang.htl.file.HtlFileType
import com.aemtools.util.psiFileFactory
import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory

/**
 * @author Dmytro Troynikov
 */
object HtlElementFactory {

    /**
     * Create [ASTNode] of [HtlTypes.VAR_NAME] type.
     *
     * @param name the name (text) of node to create
     * @param project the project
     *
     * @return new ast node
     */
    fun createVarName(name: String, project: Project): ASTNode? =
            project.psiFileFactory()
                    .file("$DOLLAR{$name}")
                    .findChildrenByType(HtlVariableName::class.java)
                    .firstOrNull()
                    ?.node
                    ?.findChildByType(HtlTypes.VAR_NAME)

    /**
     * Create [HtlPropertyAccess] element with given text.
     *
     * @param text the text
     * @param project the project
     *
     * @return new htl property access element
     */
    fun createPropertyAccess(text: String, project: Project): HtlPropertyAccess? =
            project.psiFileFactory()
                    .file("$DOLLAR{$text}")
                    .findChildrenByType(HtlPropertyAccess::class.java)
                    .firstOrNull()

    /**
     * Create [ASTNode] of [HtlTypes.DOT_ACCESS] type.
     *
     * @param text the text
     * @param project the project
     * @return new ast node
     */
    fun createDotAccessIdentifier(text: String, project: Project): ASTNode? =
            project.psiFileFactory()
                    .file("$DOLLAR{var.$text}")
                    .findChildrenByType(HtlAccessIdentifier::class.java)
                    .firstOrNull()
                    ?.node
                    ?.findChildByType(HtlTypes.DOT_ACCESS)

    /**
     * Create [ASTNode] of [HtlTypes.ARRAY_LIKE_ACCESS] type.
     * Singlequoted version.
     *
     * @param text the text
     * @param project the project
     * @return new ast node
     */
    fun createArrayLikeAccessSingleQuoted(text: String, project: Project): ASTNode? =
            project.psiFileFactory()
                    .file("$DOLLAR{var['$text']}")
                    .findChildrenByType(HtlAccessIdentifier::class.java)
                    .firstOrNull()
                    ?.node
                    ?.findChildByType(HtlTypes.ARRAY_LIKE_ACCESS)

    /**
     * Create [ASTNode] of {HtlTypes.ARRAY_LIKE_ACCESS] type.
     * Doublequoted version.
     *
     * @param text the text
     * @param project the project
     * @return new ast node
     */
    fun createArrayLikeAccessDoublequoted(text: String, project: Project): ASTNode? =
            project.psiFileFactory()
                    .file("$DOLLAR{var[\"$text\"]}")
                    .findChildrenByType(HtlAccessIdentifier::class.java)
                    .firstOrNull()
                    ?.node
                    ?.findChildByType(HtlTypes.ARRAY_LIKE_ACCESS)

    private fun PsiFileFactory.file(text: String): PsiFile
            = createFileFromText("dummy.html", HtlFileType, text)

}