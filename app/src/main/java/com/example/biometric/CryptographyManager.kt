package com.example.biometric

import javax.crypto.Cipher

interface CryptographyManager {

    /*
    This method first gets or generates an instance of secret key and initializes the cipher with the key
    The secret key uses [ENCRYPT_MODE][Cipher.ENCRYPT_MODE]
    */
    fun getInitializedCipherForEncryption(keyName: String): Cipher

    /*
    This method first gets or generates an instance of secret key and initializes the cipher with the key
    The secret key uses [DECRYPT_MODE][Cipher.DECRYPT_MODE]
    */
    fun getInitializedCipherForDecryption(keyName: String): Cipher
}