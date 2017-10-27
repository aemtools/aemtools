package com.aemtools.lang.htl.editor

import com.aemtools.lang.htl.psi.HtlTypes.DOUBLE_QUOTED_STRING
import com.aemtools.lang.htl.psi.HtlTypes.SINGLE_QUOTED_STRING
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

/**
 * @author Dmytro Troynikov
 */
class HtlQuoteHandler :
    SimpleTokenSetQuoteHandler(SINGLE_QUOTED_STRING, DOUBLE_QUOTED_STRING)
