package com.aemtools.lang.htl.editor

import com.aemtools.lang.htl.psi.HtlTypes.DOUBLE_QUOTE
import com.aemtools.lang.htl.psi.HtlTypes.SINGLE_QUOTE
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

/**
 * @author Dmytro Primshyts
 */
class HtlQuoteHandler :
    SimpleTokenSetQuoteHandler(SINGLE_QUOTE, DOUBLE_QUOTE)
