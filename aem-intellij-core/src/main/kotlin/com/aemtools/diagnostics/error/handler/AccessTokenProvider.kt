import com.aemtools.diagnostics.error.handler.TokenInitializationException
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


/**
 * @author DeusBit
 */
object AccessTokenProvider {
    private const val TOKEN_FILE_PATH = "reporting/token"
    private const val KEY: String = "MySupperKey123"
    private const val SALT: String = "MySupperSalt123"
    private const val VECTOR: String = "MySupperVector12"
    private const val KEY_LENGTH: Int = 128
    private const val ITERATION_COUNT: Int = 1024

    val token: String by lazy {
        val token = readToken()
        val decodedToken = decodeToken(token)
        decryptToken(decodedToken)
    }

    private fun readToken(): ByteArray {
        return AccessTokenProvider::class.java.getResource(TOKEN_FILE_PATH)
            .readText(StandardCharsets.UTF_8)
            .trim()
            .toByteArray()
    }

    private fun decodeToken(encodedToken: ByteArray): ByteArray {
        return Base64.getDecoder().decode(encodedToken)
    }

    private fun decryptToken(encryptedToken: ByteArray): String {
        return try {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec = PBEKeySpec(KEY.toCharArray(), SALT.toByteArray(StandardCharsets.UTF_8), ITERATION_COUNT, KEY_LENGTH)
            val tmp = factory.generateSecret(spec)
            val secret = SecretKeySpec(tmp.encoded, "AES")

            val paramSpec = IvParameterSpec(VECTOR.toByteArray(StandardCharsets.UTF_8))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secret, paramSpec)
            val plainTextBytes: ByteArray = cipher.doFinal(encryptedToken)

            String(plainTextBytes, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            throw TokenInitializationException()
        }
    }
}
