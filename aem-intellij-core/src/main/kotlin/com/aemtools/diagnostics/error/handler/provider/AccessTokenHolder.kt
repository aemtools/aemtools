package com.aemtools.diagnostics.error.handler.provider

/**
 * @author DeusBit
 */
interface AccessTokenHolder {
  /**
   * @return access token
   */
  fun getToken(): String
}
