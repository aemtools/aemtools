package com.aemtools.completion.html.inserthandler

import com.aemtools.constant.const.DOLLAR

/**
 * @author Dmytro Troynikov
 */
class HtlTemplateInsertHandler : HtlTextInsertHandler(".=\"$DOLLAR{@ param}\"", 1)