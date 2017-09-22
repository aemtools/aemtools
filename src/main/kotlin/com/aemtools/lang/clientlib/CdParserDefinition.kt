package com.aemtools.lang.clientlib

import com.aemtools.lang.clientlib.file.CdFileElementType
import com.aemtools.lang.clientlib.lexer.CdLexer
import com.aemtools.lang.clientlib.parser.CdParser
import com.aemtools.lang.clientlib.psi.CdPsiFile
import com.aemtools.lang.clientlib.psi.CdTypes
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
class CdParserDefinition : ParserDefinition {

  override fun createLexer(project: Project?): Lexer
      = CdLexer()

  override fun createParser(project: Project?): PsiParser?
      = CdParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile
      = CdPsiFile(viewProvider)

  override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements
      = ParserDefinition.SpaceRequirements.MAY

  override fun getStringLiteralElements(): TokenSet
      = TokenSet.create()

  override fun getWhitespaceTokens(): TokenSet
      = TokenSet.create(TokenType.WHITE_SPACE)

  override fun getFileNodeType(): IFileElementType?
      = CdFileElementType

  override fun createElement(node: ASTNode?): PsiElement
      = CdTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet
      = TokenSet.EMPTY

}
