package com.aemtools.lang.el

import com.aemtools.lang.el.lexer.ElLexer
import com.aemtools.lang.el.psi.ElFileElementType
import com.aemtools.lang.el.psi.ElPsiFile
import com.aemtools.lang.el.psi.ElTypes
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
 * @author Dmytro Primshyts
 */
class ElParserDefinition : ParserDefinition {
  override fun createParser(project: Project?): PsiParser = ElParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile
      = ElPsiFile(viewProvider)

  override fun spaceExistenceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements
      = ParserDefinition.SpaceRequirements.MAY

  override fun getStringLiteralElements(): TokenSet
      = TokenSet.create(ElTypes.STRING_LITERAL)

  override fun getFileNodeType(): IFileElementType
      = ElFileElementType

  override fun getWhitespaceTokens(): TokenSet
      = TokenSet.create(
      TokenType.WHITE_SPACE
  )

  override fun createLexer(project: Project?): Lexer
      = ElLexer()

  override fun createElement(node: ASTNode?): PsiElement
      = ElTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet
      = TokenSet.EMPTY

}
