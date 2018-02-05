package com.aemtools.sync.logger

import io.wcm.tooling.commons.packmgr.Logger

/**
 * @author Dmytro Liakhov
 */
class CrxStatusLogger : Logger {

  override fun warn(message: CharSequence?) = println(message)

  override fun warn(message: CharSequence?, t: Throwable?) = println(message)

  override fun info(message: CharSequence?) = println(message)

  override fun info(message: CharSequence?, t: Throwable?) = println(message)

  override fun isInfoEnabled(): Boolean = true

  override fun isErrorEnabled(): Boolean = true

  override fun error(message: CharSequence?) = println(message)

  override fun error(message: CharSequence?, t: Throwable?) = println(message)

  override fun isWarnEnabled(): Boolean = true

  override fun isDebugEnabled(): Boolean = true

  override fun debug(message: CharSequence?) = println(message)

  override fun debug(message: CharSequence?, t: Throwable?) = println(message)

}
