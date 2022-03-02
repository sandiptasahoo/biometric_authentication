package com.example.biometric

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

fun CryptographyManager() : CryptographyManager = CryptographyManagerImpl()

class CryptographyManagerImpl : CryptographyManager {

    override fun getInitializedCipherForEncryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    override fun getInitializedCipherForDecryption(keyName: String): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher
    }

    private fun getCipher(): Cipher {
        val transformation =
            "${ENCRYPTION_ALGORITHM}/${ENCRYPTION_BLOCK_MODE}/${ENCRYPTION_PADDING}"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(keyName: String): SecretKey {
        //If secret key was previously created for that ketName then return it
        val keystore = KeyStore.getInstance(ANDROID_KEYSTORE)
        // Keystore must be loaded before it can be accessed
        keystore.load(null)
        keystore.getKey(keyName, null)?.let { return it as SecretKey }

        //Generate secret key
        val paramBuilder = KeyGenParameterSpec.Builder(keyName,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        paramBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }
        val keyGenParams = paramBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    companion object CryptographyUtil {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val SECRET_KEY_NAME = "Y0UR$3CR3TK3YN@M3"
        const val KEY_SIZE = 128
        const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    }
}