package com.aemtools.lang.htl.psi

import com.aemtools.completion.util.findChildrenByType
import com.aemtools.constant.const.DOLLAR
import com.aemtools.lang.htl.file.HtlFileType
import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
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
            PsiFileFactory.getInstance(project)
                    .createFileFromText(
                            "dummy.html",
                            HtlFileType,
                            "$DOLLAR{$name}")
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
            PsiFileFactory.getInstance(project)
                    .createFileFromText(
                            "dummy.html",
                            HtlFileType,
                            "$DOLLAR{$text}"
                    ).findChildrenByType(HtlPropertyAccess::class.java)
                    .firstOrNull()

}