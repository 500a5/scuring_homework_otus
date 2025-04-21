package soft.divan.world.chill.scuring_homework_otus

import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import soft.divan.world.chill.scuring_homework_otus.crypto.Keys
import soft.divan.world.chill.scuring_homework_otus.crypto.Security
import soft.divan.world.chill.scuring_homework_otus.storage.PreferencesUtils

@Composable
fun MainScreen(
    secure: Security,
    key: Keys,
    preferences: PreferencesUtils,
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }
    val biometricHelper = BiometricHelper(LocalContext.current as FragmentActivity)
    val onBiometric = remember { mutableStateOf(preferences.getBoolean("biometric")) }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = modifier
        )

        Button(onClick = {
            preferences.setString("key", secure.encryptAes(inputValue, key.getAesSecretKey()))


        }) {
            Text(text = "Set")
        }

        Button(onClick = {
            inputValue = secure.decryptAes(preferences.getString("key"), key.getAesSecretKey())
        }) {
            Text(text = "Get")
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (  biometricHelper.canAuthenticate()  == BiometricManager.BIOMETRIC_SUCCESS) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = onBiometric.value,
                    onCheckedChange = {
                        onBiometric.value = it
                        preferences.setBoolean("biometric", it)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Включить биометрию")
            }
        }
    }

}