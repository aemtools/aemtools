package com.aemtools.diagnostics.error.handler.provider.impl

import com.aemtools.diagnostics.error.handler.ErrorReportingConfig
import com.aemtools.diagnostics.error.handler.crypto.TokenDecryptService
import com.aemtools.diagnostics.error.handler.provider.AccessTokenHolder
import java.nio.charset.StandardCharsets

/**
 * @author DeusBit
 */
class GitHubAccessTokenHolder : AccessTokenHolder {

  override fun getToken(): String {
    return GitHubAccessTokenProvider.token
  }

  object GitHubAccessTokenProvider {
    private val config = ErrorReportingConfig()

    val token: String by lazy {
      val token = readToken()
      String(TokenDecryptService.decrypt(token))
    }

    private fun readToken(): ByteArray {
      return GitHubAccessTokenProvider::class.java.classLoader.getResource(config.tokenFile)
          .readText(StandardCharsets.UTF_8)
          .trim()
          .toByteArray()
    }
  }
}

