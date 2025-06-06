package soft.divan.world.chill.scuring_homework_otus.data.encryption

import javax.crypto.SecretKey

interface KeyManager {
    fun removeKeys(keyAlias: String)
    fun getAesSecretKey(): SecretKey
}