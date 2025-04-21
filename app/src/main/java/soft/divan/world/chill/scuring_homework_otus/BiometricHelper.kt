package soft.divan.world.chill.scuring_homework_otus

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricHelper(
    private val context: Context,
    private val executor: Executor = ContextCompat.getMainExecutor(context)
) {
    interface BiometricCallback {
        fun onAuthenticationSuccess()
        fun onAuthenticationFailed()
        fun onAuthenticationError(error: String)
    }

    fun canAuthenticate(): Int {
        return BiometricManager.from(context).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
        )
    }

    fun showBiometricPrompt(callback: BiometricCallback) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Биометрическая аутентификация")
            .setSubtitle("Войдите с помощью биометрии")
            .setNegativeButtonText("Отмена")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            .build()

        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    callback.onAuthenticationSuccess()
                }

                override fun onAuthenticationFailed() {
                    callback.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    callback.onAuthenticationError(errString.toString())
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}
