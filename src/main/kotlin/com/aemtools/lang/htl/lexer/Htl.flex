package com.aemtools.lang.htl.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.*;
import com.intellij.psi.xml.*;
import com.intellij.lexer.FlexLexer;
import static com.aemtools.lang.htl.psi.HtlTypes.*;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%unicode

%{
  public _HtlLexer() {
    this((java.io.Reader)null);
  }
%}

%class _HtlLexer
%public
%implements FlexLexer
%function advance
%type IElementType
//%debug

%state EL

WHITE_SPACE_CHARS=[ \n\r\t\f]+

EL_START= "${"

DOUBLE_QUOTED_STRING=\"((\\[\"btnfr])|[^\"\n])*\"?
SINGLE_QUOTED_STRING='((\\['btnfr])|[^'\n])*'?
INTEGER=[0-9]+
VAR_NAME=[\w\d:]+

%%

<YYINITIAL> {EL_START} {
    yybegin(EL);
    return EL_START;
}

<YYINITIAL> [^] { return OUTER_LANGUAGE; }

<EL> "}" { yybegin(YYINITIAL); return RBRACE; }
<EL> {
   {WHITE_SPACE_CHARS}           { return WHITE_SPACE; }

     "{"                         { return LBRACE; }
     "("                         { return LBRACKET; }
     ")"                         { return RBRACKET; }
     "["                         { return LSQRBRACKET; }
     "]"                         { return RSQRBRACKET; }
     "?"                         { return QUESTION; }
     ":"                         { return COLON; }
     "=="                        { return EQ; }
     "="                         { return SINGLE_EQ; }
     "!="                        { return NEQ; }
     ">"                         { return GT; }
     "<"                         { return LT; }
     ">="                        { return GTE; }
     "<="                        { return LTE; }
     "${"                        { return EL_START; }
     "-"                         { return MINUS; }
     "+"                         { return PLUS; }
     "!"                         { return EXCLAMATION; }
     ","                         { return COMMA; }
     "."                         { return DOT; }
     "*"                         { return MULTIPLY; }
     "&&"                        { return AND_AND; }
     "||"                        { return OR_OR; }
     "@"                         { return AT; }
     "'"                         { return SQUOT; }
     "true"                      { return TRUE; }
     "false"                     { return FALSE; }
     "DIVIDE"                    { return DIVIDE; }

     {DOUBLE_QUOTED_STRING}      { return DOUBLE_QUOTED_STRING; }
     {SINGLE_QUOTED_STRING}      { return SINGLE_QUOTED_STRING; }
     {INTEGER}                   { return INTEGER; }
     {VAR_NAME}                  { return VAR_NAME; }

     [^]                         { yybegin(YYINITIAL); return OUTER_LANGUAGE; }
}