package soft.divan.world.chill.scuring_homework_otus.data.encryption

import android.util.Base64
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec


class DataStoreEncryption(
    private val keyManager: KeyManager
) {

    fun encryptData(data: String?): String {
        val key = keyManager.getAesSecretKey()
        val encrypted = encryptAes(data!!, key)
        return encrypted
    }

    fun decryptData(data: String): String {
        val key = keyManager.getAesSecretKey()
        val decrypted = decryptAes(data, key)
        return decrypted
    }

    private fun encryptAes(plainText: String, key: Key): String {
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val ivSpec = GCMParameterSpec(AUTH_TAG_SIZE, iv)
        val cipher = makeCipher(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encodedBytes = cipher.doFinal(plainText.toByteArray())

        val combined = iv + encodedBytes
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    private fun makeCipher(opMode: Int, key: Key, ivSpec: GCMParameterSpec): Cipher {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(opMode, key, ivSpec)
        return cipher
    }

    private fun decryptAes(encrypted: String, key: Key): String {
        val combined = Base64.decode(encrypted, Base64.NO_WRAP)

        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encryptedBytes = combined.copyOfRange(GCM_IV_LENGTH, combined.size)

        val ivSpec = GCMParameterSpec(AUTH_TAG_SIZE, iv)
        val cipher = makeCipher(Cipher.DECRYPT_MODE, key, ivSpec)

        val decoded = cipher.doFinal(encryptedBytes)
        return String(decoded, Charsets.UTF_8)
    }

    companion object {
        private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val AUTH_TAG_SIZE = 128
    }

}