package com.aemtools.diagnostics.error.handler.crypto

import com.aemtools.diagnostics.error.handler.exception.TokenInitializationException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author Kostiantyn Diachenko
 */
object TokenDecryptService {
  private const val KEY: String = "MySupperKey123"
  private const val SALT: String = "MySupperSalt123"
  private const val VECTOR: String = "MySupperVector12"
  private const val KEY_LENGTH: Int = 128
  private const val ITERATION_COUNT: Int = 1024

  fun decrypt(decryptedText: ByteArray): ByteArray {
    val decodedToken = decodeToken(decryptedText)
    return String(decryptToken(decodedToken)).toByteArray()
  }

  private fun decodeToken(encodedToken: ByteArray): ByteArray {
    return Base64.getDecoder().decode(encodedToken)
  }

  private fun decryptToken(encryptedToken: ByteArray): ByteArray {
    return try {
      val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
      val spec = PBEKeySpec(
          KEY.toCharArray(),
          SALT.toByteArray(StandardCharsets.UTF_8),
          ITERATION_COUNT,
          KEY_LENGTH
      )
      val tmp = factory.generateSecret(spec)
      val secret = SecretKeySpec(tmp.encoded, "AES")

      val paramSpec = IvParameterSpec(VECTOR.toByteArray(StandardCharsets.UTF_8))
      val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
      cipher.init(Cipher.DECRYPT_MODE, secret, paramSpec)

      cipher.doFinal(encryptedToken)
    } catch (ex: Exception) {
      throw TokenInitializationException()
    }
  }
}
