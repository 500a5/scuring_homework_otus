package soft.divan.world.chill.scuring_homework_otus.data.encryption

import android.content.Context
import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class KeyManagerLowerThanM @Inject constructor(
    private val applicationContext: Context,
) : KeyManagerImpl() {

    private val rsaKeys = RSAKeysLowerThanM(applicationContext, keyStore)

    private val sharedPreferences by lazy {
        applicationContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    override fun getAesSecretKey(): SecretKey {
        return getAesSecretKeyLessThanM() ?: generateAesSecretKey()
    }

    private fun getAesSecretKeyLessThanM(): SecretKey? {
        val encryptedKeyBase64Encoded = getSecretKeyFromSharedPreferences()
        return encryptedKeyBase64Encoded?.let {
            val encryptedKey = Base64.decode(encryptedKeyBase64Encoded, Base64.DEFAULT)
            val key = rsaDecryptKey(encryptedKey)
            SecretKeySpec(key, AES_ALGORITHM)
        }
    }

    private fun generateAesSecretKey(): SecretKey {
        return generateAndSaveAesSecretKeyLessThanM()
    }

    private fun getSecretKeyFromSharedPreferences(): String? {
        return sharedPreferences.getString(ENCRYPTED_KEY_NAME, null)
    }

    private fun generateAndSaveAesSecretKeyLessThanM(): SecretKey {
        val key = ByteArray(16)
        SecureRandom().run {
            nextBytes(key)
        }
        val encryptedKeyBase64encoded = Base64.encodeToString(
            rsaEncryptKey(key),
            Base64.DEFAULT
        )
        sharedPreferences.edit().apply {
            putString(ENCRYPTED_KEY_NAME, encryptedKeyBase64encoded)
            apply()
        }
        return SecretKeySpec(key, AES_ALGORITHM)
    }

    private fun rsaEncryptKey(secret: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(RSA_MODE_LESS_THAN_M)
        cipher.init(Cipher.ENCRYPT_MODE, rsaKeys.getRsaPublicKey())
        return cipher.doFinal(secret)
    }

    private fun rsaDecryptKey(encryptedKey: ByteArray?): ByteArray {
        val cipher = Cipher.getInstance(RSA_MODE_LESS_THAN_M)
        cipher.init(Cipher.DECRYPT_MODE, rsaKeys.getRsaPrivateKey())
        return cipher.doFinal(encryptedKey)
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "RSAEncryptedKeysSharedPreferences"
        private const val ENCRYPTED_KEY_NAME = "RSAEncryptedKeysKeyName"
        private const val RSA_MODE_LESS_THAN_M = "RSA/ECB/PKCS1Padding"
    }

}