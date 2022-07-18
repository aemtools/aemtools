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
%state COMMENT
%state SQ
%state DQ

WHITE_SPACE_CHARS=[ \n\r\t\f]+

EL_START= "${"

INTEGER=[0-9]+
FLOAT=\d+([\.]\d*)?([eE][+-]?\d+)?
VAR_NAME=[\w\d:]+
COMMENT_START="<!--/*"
COMMENT_END="*/-->"

DOUBLE_QUOTED_STRING_BOUNDARY="\""
SINGLE_QUOTED_STRING_BOUNDARY="'"

VALID_ESCAPE=(\\t)|(\\b)|(\\n)|(\\r)|(\\f)|(\\\\)

UNICODE_ESCAPE=(\\u[\da-f]{4})

SQ_QUOTE_ESCAPE=(\\')
DQ_QUOTE_ESCAPE=(\\\")

INVALID_UNICODE_ESCAPE=(\\u[0-9a-f]{3}?)
INVALID_CHARACTER_ESCAPE=(\\\w)

STRING_CONTENT=.

%%

<YYINITIAL> {EL_START} {
    yybegin(EL);
    return EL_START;
}

<YYINITIAL> {COMMENT_START} {
    yybegin(COMMENT);
    return COMMENT_START;
}

<YYINITIAL> [^] { return OUTER_LANGUAGE; }
<COMMENT> {COMMENT_END} {
    yybegin(YYINITIAL);
    return COMMENT_END;
}

<COMMENT> [^] {
    return COMMENT_TOKEN;
}

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
     "true"                      { return TRUE; }
     "false"                     { return FALSE; }
     "null"                      { return NULL_LITERAL_TOKEN; }
     "in"                        { return IN; }
     {DOUBLE_QUOTED_STRING_BOUNDARY} {
        yybegin(DQ); return DOUBLE_QUOTE;
     }
     {SINGLE_QUOTED_STRING_BOUNDARY} {
        yybegin(SQ); return SINGLE_QUOTE;
     }
     {INTEGER}                   { return INTEGER; }
     {FLOAT}                     { return FLOAT; }
     {VAR_NAME}                  { return VAR_NAME; }

     [^]                         { yybegin(YYINITIAL); return OUTER_LANGUAGE; }
}

<DQ> {
    {VALID_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {UNICODE_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {DQ_QUOTE_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {INVALID_UNICODE_ESCAPE} {
        return INVALID_UNICODE_ESCAPE_TOKEN;
    }
    {SQ_QUOTE_ESCAPE} {
        return INVALID_CHARACTER_ESCAPE_TOKEN;
    }
    {INVALID_CHARACTER_ESCAPE} {
        return INVALID_CHARACTER_ESCAPE_TOKEN;
    }
    {DOUBLE_QUOTED_STRING_BOUNDARY} {
        yybegin(EL);
        return DOUBLE_QUOTE;
    }
    {STRING_CONTENT} {
        return STRING_CONTENT;
    }
    [^] {
        yypushback(1);
        yybegin(EL);
    }
}

<SQ> {
    {VALID_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {UNICODE_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {SQ_QUOTE_ESCAPE} {
        return VALID_STRING_ESCAPE_TOKEN;
    }
    {INVALID_UNICODE_ESCAPE} {
        return INVALID_UNICODE_ESCAPE_TOKEN;
    }
    {DQ_QUOTE_ESCAPE} {
        return INVALID_CHARACTER_ESCAPE_TOKEN;
    }
    {INVALID_CHARACTER_ESCAPE} {
        return INVALID_CHARACTER_ESCAPE_TOKEN;
    }
    {SINGLE_QUOTED_STRING_BOUNDARY} {
        yybegin(EL);
        return SINGLE_QUOTE;
    }
    {STRING_CONTENT} {
        return STRING_CONTENT;
    }
    [^] {
        yypushback(1);
        yybegin(EL);
    }
}
