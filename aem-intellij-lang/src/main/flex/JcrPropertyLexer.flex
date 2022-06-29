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

  boolean isLastArrayValueTokenEndsWithRbracket() {
      return zzMarkedPos == zzEndRead && yytext().toString().endsWith("]");
  }
%}

%public
%class _JcrPropertyLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%state TYPE
%state VALUE
%state ARRAY

EOL=\R
WHITE_SPACE=\s+

VALUE_TOKEN=.
ARRAY_VALUE_TOKEN=.

XML_ENTITY_ESCAPE=&(quot|amp|apos|lt|gt);
XML_DECIMAL_ESCAPE=&#[0-9]+;
XML_HEX_ESCAPE=&#x[0-9a-fA-F]+;
XML_ATTRIBUTE_VALUE_SPECIAL_CHARACTERS_ESCAPE=\\[,\[\]\{\"\'\}]
VALID_ESCAPE=(\\t)|(\\b)|(\\n)|(\\r)|(\\f)|(\\\\)
UNICODE_ESCAPE=(\\u[\da-f]{4})
INVALID_UNICODE_ESCAPE=(\\u[0-9a-f]{3}?)
INVALID_CHARACTER_ESCAPE=(\\\w)

%%
<YYINITIAL> {
    "{" {
        yybegin(TYPE);
        return LBRACE;
    }
    "[" {
        yybegin(ARRAY);
        return LBRACKET;
    }
    "]" {
        return RBRACKET;
    }
    [^] {
        yypushback(1);
        yybegin(VALUE);
    }
}

<TYPE> {
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
    "}"                  {
        yybegin(YYINITIAL);
        return RBRACE;
    }
    [^] {
        yypushback(1);
        yybegin(YYINITIAL);
    }
}

<VALUE> {
    {XML_ENTITY_ESCAPE}                               { return VALID_XML_ENTITY_REF_ESCAPE_TOKEN; }
    {XML_DECIMAL_ESCAPE}                              { return VALID_XML_CHAR_REF_ESCAPE_TOKEN; }
    {XML_HEX_ESCAPE}                                  { return VALID_XML_CHAR_REF_ESCAPE_TOKEN; }
    {XML_ATTRIBUTE_VALUE_SPECIAL_CHARACTERS_ESCAPE}   { return VALID_STRING_ESCAPE_TOKEN; }
    {VALID_ESCAPE}                                    { return VALID_STRING_ESCAPE_TOKEN; }
    {UNICODE_ESCAPE}                                  { return VALID_STRING_ESCAPE_TOKEN; }
    {INVALID_UNICODE_ESCAPE}                          { return INVALID_UNICODE_ESCAPE_TOKEN; }
    {INVALID_CHARACTER_ESCAPE}                        { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    {VALUE_TOKEN}                                     { return VALUE_TOKEN; }
}

<ARRAY> {
    ","                                               { return COMMA; }
    {XML_ENTITY_ESCAPE}                               { return VALID_XML_ENTITY_REF_ESCAPE_TOKEN; }
    {XML_DECIMAL_ESCAPE}                              { return VALID_XML_CHAR_REF_ESCAPE_TOKEN; }
    {XML_HEX_ESCAPE}                                  { return VALID_XML_CHAR_REF_ESCAPE_TOKEN; }
    {XML_ATTRIBUTE_VALUE_SPECIAL_CHARACTERS_ESCAPE}   { return VALID_STRING_ESCAPE_TOKEN; }
    {VALID_ESCAPE}                                    { return VALID_STRING_ESCAPE_TOKEN; }
    {UNICODE_ESCAPE}                                  { return VALID_STRING_ESCAPE_TOKEN; }
    {INVALID_UNICODE_ESCAPE}                          { return INVALID_UNICODE_ESCAPE_TOKEN; }
    {INVALID_CHARACTER_ESCAPE}                        { return INVALID_CHARACTER_ESCAPE_TOKEN; }
    {ARRAY_VALUE_TOKEN}        {
        if (isLastArrayValueTokenEndsWithRbracket()) {
          yypushback(1);
          yybegin(YYINITIAL);
          break;
        }
        return ARRAY_VALUE_TOKEN;
    }
}
