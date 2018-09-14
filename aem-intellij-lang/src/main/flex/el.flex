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

LITERAL_EXPRESSION_TOKEN=(~(\$\{))|(~(#\{))
INTEGER_LITERAL=[0-9]([0-9])*

FLOATING_POINT_LITERAL=(({DIGIT})+\.({DIGIT})*{EXPONENT}?)|(\.({DIGIT})+{EXPONENT})|({DIGIT}+{EXPONENT})

DIGIT=[0-9]

EXPONENT=[eE]([-+]?)({DIGIT})+

STRING_LITERAL={DOUBLE_QUOTED_STRING}|{SINGLE_QUOTED_STRING}

DOUBLE_QUOTED_STRING=\"(( [^\"\\]) | (\\ ([\\\"\'])))* \"?
SINGLE_QUOTED_STRING=\'(( [^\'\\]) | (\\([\\\"\'])))* \'?

CONCAT=(\+=)

ASSIGN=\=

ARROW=->

IDENTIFIER=({LETTER}|{IMPL_OBJ_START})({LETTER}|{DIGIT})*

FUNCTION_SUFFIX={IDENTIFIER}

IMPL_OBJ_START=#

LETTER=\w

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
  "ne"                        { return NE1; }
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
  "null"                      { return NULL_LITERAL_TOKEN; }
  {LITERAL_EXPRESSION_TOKEN}  { return LITERAL_EXPRESSION_TOKEN; }
  {INTEGER_LITERAL}           { return INTEGER_LITERAL; }
  {IDENTIFIER}                { return IDENTIFIER_TOKEN; }
  {FUNCTION_SUFFIX}           { return FUNCTION_SUFFIX; }
  {FLOATING_POINT_LITERAL}    { return FLOATING_POINT_LITERAL; }
  {STRING_LITERAL}            { return STRING_LITERAL; }


}

[^] { return BAD_CHARACTER; }
