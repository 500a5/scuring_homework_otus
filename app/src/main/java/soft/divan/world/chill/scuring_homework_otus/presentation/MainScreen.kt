package soft.divan.world.chill.scuring_homework_otus.presentation

import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import soft.divan.world.chill.scuring_homework_otus.biometrics.BiometricHelper

import soft.divan.world.chill.scuring_homework_otus.data.storage.PreferencesUtils

@Composable
fun MainScreen(
    preferences: PreferencesUtils,
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }
    val context = LocalContext.current as FragmentActivity
    val biometricHelper = remember { BiometricHelper(context) }

    // Подписываемся на Flow из PreferencesUtils
    val stringData by preferences.stringDataFlow.collectAsState(initial = null)
    val booleanData by preferences.booleanDataFlow.collectAsState(initial = false)

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = modifier
        )

        Button(onClick = {
            // Сохраняем зашифрованную строку
            CoroutineScope(Dispatchers.IO).launch {
                preferences.setString(inputValue)
            }
        }) {
            Text(text = "Set")
        }

        Button(onClick = {
            // Расшифровываем строку из preferences
            inputValue = stringData ?: ""
        }) {
            Text(text = "Get")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (biometricHelper.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = booleanData == true,
                    onCheckedChange = { isChecked ->
                        // Сохраняем boolean значение
                        CoroutineScope(Dispatchers.IO).launch {
                            preferences.setBoolean(isChecked)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Включить биометрию")
            }
        }
    }
}
