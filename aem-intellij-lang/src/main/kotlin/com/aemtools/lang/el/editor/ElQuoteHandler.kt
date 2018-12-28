package com.aemtools.lang.el.editor

import com.aemtools.lang.el.psi.ElTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

/**
 * @author Dmytro Primshyts
 */
class ElQuoteHandler :
    SimpleTokenSetQuoteHandler(ElTypes.STRING_LITERAL)
