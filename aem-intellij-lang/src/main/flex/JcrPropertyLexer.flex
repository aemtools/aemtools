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

VALUE_TOKEN=.*
ARRAY_VALUE_TOKEN=([^,](\\,)?)+

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
    /* Start: Block to cover brace matcher functionality */
    "]" {
        return RBRACKET;
    }
    /* End: Block to cover brace matcher functionality */
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
    {VALUE_TOKEN}        { return VALUE_TOKEN; }
}

<ARRAY> {
    ","                  { return COMMA; }
    "]"                  { return RBRACKET; }
    {ARRAY_VALUE_TOKEN}  {
        if (isLastArrayValueTokenEndsWithRbracket()) {
            yypushback(1);
            yybegin(ARRAY);
        }
        return ARRAY_VALUE_TOKEN;
    }
    [^] {
        yypushback(1);
        yybegin(YYINITIAL);
    }
}
