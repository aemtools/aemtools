package com.aemtools.diagnostics.error.handler

/**
 * @author DeusBit
 */
interface AccessTokenHolder {
  /**
   * @return access token
   */
  fun getToken(): String
}
