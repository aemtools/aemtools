package com.aemtools.lang.el;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.aemtools.lang.el.psi.ElTypes.*;

%%

%{
  public _ElLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _ElLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "#{"                        { return START_DEFERRED_EXPRESSION; }
  "${"                        { return START_DYNAMIC_EXPRESSION; }
  "}"                         { return END_EXPRESSION; }
  "?"                         { return QUESTION_MARK; }
  ":"                         { return COLON; }
  "||"                        { return OR0; }
  "or"                        { return OR1; }
  "&&"                        { return AND0; }
  "and"                       { return AND1; }
  "=="                        { return EQ0; }
  "eq"                        { return EQ1; }
  "!="                        { return NE0; }
  "neq"                       { return NE1; }
  "<"                         { return LT0; }
  "lt"                        { return LT1; }
  ">"                         { return GT0; }
  "gt"                        { return GT1; }
  "<="                        { return LE0; }
  "le"                        { return LE1; }
  ">="                        { return GE0; }
  "ge"                        { return GE1; }
  "+"                         { return PLUS; }
  "-"                         { return MINUS; }
  "*"                         { return MULT; }
  "/"                         { return DIV; }
  "%"                         { return MOD0; }
  "mod"                       { return MOD1; }
  "!"                         { return NOT0; }
  "not"                       { return NOT1; }
  "empty"                     { return EMPTY; }
  "."                         { return DOT; }
  "["                         { return LBRACK; }
  "]"                         { return RBRACK; }
  "("                         { return LPAREN; }
  ")"                         { return RPAREN; }
  ","                         { return COMMA; }
  "true"                      { return TRUE; }
  "false"                     { return FALSE; }
  "null"                      { return NULL_LITERAL; }
  "LITERAL_EXPRESSION"        { return LITERAL_EXPRESSION; }
  "IDENTIFIER"                { return IDENTIFIER; }
  "FUNCTION_SUFFIX"           { return FUNCTION_SUFFIX; }
  "FLOATING_POINT_LITERAL"    { return FLOATING_POINT_LITERAL; }
  "INTEGER_LITERAL"           { return INTEGER_LITERAL; }
  "STRING_LITERAL"            { return STRING_LITERAL; }


}

[^] { return BAD_CHARACTER; }