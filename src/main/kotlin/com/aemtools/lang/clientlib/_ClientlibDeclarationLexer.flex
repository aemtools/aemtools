package com.aemtools.lang.clientlib;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.aemtools.lang.clientlib.psi.CdTypes.*;

%%

%{
  public _ClientlibDeclarationLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _ClientlibDeclarationLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SEPARATOR=\\|"/"
WORD=[\w\d_.-]+
COMMENT_TOKEN=(#[ \t\n\x0B\f\r].*)|(##.*)

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "."                  { return DOT; }
  ".."                 { return DD; }
  "#base="             { return PREFIX_TOKEN; }

  {SEPARATOR}          { return SEPARATOR; }
  {WORD}               { return WORD; }
  {COMMENT_TOKEN}      { return COMMENT_TOKEN; }

}

[^] { return BAD_CHARACTER; }
