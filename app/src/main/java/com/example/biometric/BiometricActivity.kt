package com.example.biometric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class BiometricActivity : AppCompatActivity() {
    private lateinit var biometricPrompt : BiometricPrompt
    private lateinit var button: Button
    private lateinit var cryptographyManager: CryptographyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata_flow_example)

        cryptographyManager = CryptographyManager()
        button = findViewById(R.id.loginBtn)
        biometricPrompt = createBiometricPrompt()

        button.setOnClickListener{
            val promptInfo = createPromptInfo()
            Log.d(TAG, "${BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG)}")
            if(BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS){
                val cipher = cryptographyManager.getInitializedCipherForEncryption(SECRET_KEY_NAME)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "$errorCode :: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "Authentication Successful!")
                Toast.makeText(applicationContext, R.string.auth_success, Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication failed!")
            }
        }
        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("HDFC Bank")
            .setSubtitle("Log in using your biometric credential")
            .setDescription("Description")
            .setNegativeButtonText("Use account password")
            .build()
    }

    companion object {
        private val TAG = BiometricActivity::class.java.simpleName
        private const val SECRET_KEY_NAME = "Y0UR$3CR3TK3YN@M3"
    }
}
