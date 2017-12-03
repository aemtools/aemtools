package com.aemtools.lang.htl

import com.aemtools.lang.htl.core.HtlFileElementType
import com.aemtools.lang.htl.lexer.HtlLexer
import com.aemtools.lang.htl.parser.HtlParser
import com.aemtools.lang.htl.psi.HtlPsiFile
import com.aemtools.lang.htl.psi.HtlTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

/**
 * @author Dmytro_Troynikov
 */
class HtlParserDefinition : ParserDefinition {

  override fun createLexer(project: Project?): Lexer
      = HtlLexer()

  override fun createParser(project: Project?): PsiParser?
      = HtlParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile?
      = HtlPsiFile(viewProvider)

  override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements?
      = ParserDefinition.SpaceRequirements.MUST

  override fun getStringLiteralElements(): TokenSet
      = TokenSet.create(
      HtlTypes.SINGLE_QUOTED_STRING,
      HtlTypes.DOUBLE_QUOTED_STRING
  )

  override fun getWhitespaceTokens(): TokenSet
      = TokenSet.create(TokenType.WHITE_SPACE)

  override fun getFileNodeType(): IFileElementType?
      = HtlFileElementType

  override fun createElement(node: ASTNode?): PsiElement
      = HtlTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet
      = TokenSet.EMPTY

}
