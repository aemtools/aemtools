package com.aemtools.lang.jcrproperty;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.aemtools.lang.jcrproperty.psi.JpTypes.*;

%%

%{
  public _JcrPropertyLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _JcrPropertyLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%state ARRAY

EOL=\R
WHITE_SPACE=\s+

VALUE_TOKEN=[^\[\]{}]+
ARRAY_VALUE_TOKEN=[^\[\]{},]+

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "String"             { return STRING; }
  "Binary"             { return BINARY; }
  "Long"               { return LONG; }
  "Double"             { return DOUBLE; }
  "Decimal"            { return DECIMAL; }
  "Date"               { return DATE; }
  "Boolean"            { return BOOLEAN; }
  "Name"               { return NAME; }
  "Path"               { return PATH; }
  "Reference"          { return REFERENCE; }
  "WeakReference"      { return WEAK_REFERENCE; }
  "URI"                { return URI; }
  "{"                  { return LBRACE; }
  "}"                  { return RBRACE; }
  "["                  {
    yybegin(ARRAY);
    return LBRACKET;
  }
  "]"                  { return RBRACKET; }
  ","                  { return COMMA; }

  {VALUE_TOKEN}        { return VALUE_TOKEN; }

}

<ARRAY> {
{WHITE_SPACE}        { return WHITE_SPACE; }

  "String"             { return STRING; }
  "Binary"             { return BINARY; }
  "Long"               { return LONG; }
  "Double"             { return DOUBLE; }
  "Decimal"            { return DECIMAL; }
  "Date"               { return DATE; }
  "Boolean"            { return BOOLEAN; }
  "Name"               { return NAME; }
  "Path"               { return PATH; }
  "Reference"          { return REFERENCE; }
  "WeakReference"      { return WEAK_REFERENCE; }
  "URI"                { return URI; }
  "{"                  { return LBRACE; }
  "}"                  { return RBRACE; }
  "["                  { return LBRACKET; }
  "]"                  {
    yybegin(YYINITIAL);
    return RBRACKET;
  }
  ","                  { return COMMA; }
  {ARRAY_VALUE_TOKEN}  { return ARRAY_VALUE_TOKEN; }
}

[^] { return BAD_CHARACTER; }
