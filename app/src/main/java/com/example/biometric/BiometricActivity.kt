package com.example.biometric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class BiometricActivity : AppCompatActivity() {
    private lateinit var biometricPrompt : BiometricPrompt
    private val TAG = BiometricActivity::class.java.simpleName
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.loginBtn)
        biometricPrompt = createBiometricPrompt()

        button.setOnClickListener{
            val promptInfo = createPromptInfo()
            Log.d(TAG, "${BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG)}")
            if(BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS){
                biometricPrompt.authenticate(promptInfo)
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
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication failed!")
            }
        }
        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo() : BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("HDFC Bank")
            .setSubtitle("Log in using your biometric credential")
            .setDescription("Description")
            .setNegativeButtonText("Use account password")
            .build()
    }
}
