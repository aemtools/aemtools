package com.aemtools.completion.html.inserthandler

/**
 * Htl identifier insert handler.
 *
 * Adds `.<caret>=""` upon trigger.
 */
class HtlIdentifierInsertHandler : HtlTextInsertHandler(".=\"\"", offset = 1)
