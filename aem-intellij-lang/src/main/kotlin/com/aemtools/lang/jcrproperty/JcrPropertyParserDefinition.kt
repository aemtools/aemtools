package com.aemtools.lang.jcrproperty

import com.aemtools.lang.jcrproperty.file.JpFileElementType
import com.aemtools.lang.jcrproperty.file.JpPsiFile
import com.aemtools.lang.jcrproperty.lexer.JpLexer
import com.aemtools.lang.jcrproperty.parser.JpParser
import com.aemtools.lang.jcrproperty.psi.JpTypes
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
class JcrPropertyParserDefinition : ParserDefinition {

  override fun createLexer(project: Project?): Lexer = JpLexer()
  override fun createParser(project: Project?): PsiParser = JpParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile =
      JpPsiFile(viewProvider)

  override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements
      = ParserDefinition.SpaceRequirements.MAY

  override fun getStringLiteralElements(): TokenSet = TokenSet.create(
      JpTypes.VALUE_TOKEN, JpTypes.ARRAY_VALUE_TOKEN
  )

  override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

  override fun getFileNodeType(): IFileElementType = JpFileElementType

  override fun createElement(node: ASTNode?): PsiElement = JpTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet = TokenSet.EMPTY
}
