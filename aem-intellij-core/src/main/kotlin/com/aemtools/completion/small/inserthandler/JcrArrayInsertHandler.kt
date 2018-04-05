package com.aemtools.completion.small.inserthandler

import com.aemtools.completion.html.inserthandler.HtlTextInsertHandler

/**
 * @author Dmytro Primshyts
 */
class JcrArrayInsertHandler
  : HtlTextInsertHandler("=\"[]\"", offset = 3)
