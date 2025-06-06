package soft.divan.world.chill.scuring_homework_otus.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import soft.divan.world.chill.scuring_homework_otus.biometrics.BiometricHelper
import soft.divan.world.chill.scuring_homework_otus.data.storage.PreferencesUtils

@Composable
fun AuthenticationScreen(
    preferences: PreferencesUtils,
    context: Context = LocalContext.current,
    onAuthSuccess: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val biometricHelper = BiometricHelper(LocalContext.current as FragmentActivity)

    // Подписываемся на booleanDataFlow
    val biometricEnabled by preferences.booleanDataFlow.collectAsState(initial = false)

    // Если biometric включен — показываем биометрию 1 раз при входе
    LaunchedEffect(biometricEnabled) {
        if (biometricEnabled == true) {
            showBiometricPrompt(biometricHelper, onAuthSuccess, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.value == "otus@test.com" && password.value == "otus") {
                    onAuthSuccess()
                } else {
                    Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (biometricEnabled == true) {
            Button(
                onClick = {
                    showBiometricPrompt(biometricHelper, onAuthSuccess, context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти через биометрию")
            }
        }
    }
}
private fun showBiometricPrompt(
    biometricHelper: BiometricHelper,
    onAuthSuccess: () -> Unit,
    context: Context
) {
    biometricHelper.showBiometricPrompt(object : BiometricHelper.BiometricCallback {
        override fun onAuthenticationSuccess() {
            onAuthSuccess()
        }

        override fun onAuthenticationFailed() {
            Toast.makeText(context, "Не удалось распознать", Toast.LENGTH_SHORT).show()
        }

        override fun onAuthenticationError(error: String) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    })
}