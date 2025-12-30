package com.aemtools.completion.html.inserthandler

/**
 * Htl expression insert handler.
 *
 * Adds `="${<caret>}"` upon trigger.
 */
class HtlExpressionInsertHandler :
    HtlTextInsertHandler("=\"\${}\"", offset = 4)
