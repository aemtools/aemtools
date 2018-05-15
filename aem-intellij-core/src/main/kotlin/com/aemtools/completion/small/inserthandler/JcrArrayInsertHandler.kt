package com.aemtools.completion.small.inserthandler

import com.aemtools.completion.html.inserthandler.HtlTextInsertHandler

/**
 * @author Dmytro Primshyts
 */
class JcrArrayInsertHandler
  : HtlTextInsertHandler("=\"[]\"", offset = 3)

/**
 * Jcr name array insert handler.
 */
class JcrNameArrayInsertHandler
  : HtlTextInsertHandler("=\"{Name}[]\"", offset = 9)
