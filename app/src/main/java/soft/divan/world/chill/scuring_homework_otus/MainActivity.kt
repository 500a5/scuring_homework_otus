package soft.divan.world.chill.scuring_homework_otus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.security.crypto.MasterKey
import soft.divan.world.chill.scuring_homework_otus.storage.PreferencesUtils
import soft.divan.world.chill.scuring_homework_otus.crypto.Keys
import soft.divan.world.chill.scuring_homework_otus.crypto.Security
import soft.divan.world.chill.scuring_homework_otus.ui.theme.Scuring_homework_otusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val secure = Security()
        val keys = Keys(applicationContext)
        val masterKey = keys.getMasterKey(MasterKey.KeyScheme.AES256_GCM)
        val preferences = PreferencesUtils(applicationContext, masterKey)

        enableEdgeToEdge()
        setContent {
            Scuring_homework_otusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        secure = secure,
                        key = keys,
                        preferences = preferences,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


    }
}

@Composable
fun MainScreen(
    secure: Security,
    key: Keys,
    preferences: PreferencesUtils,
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }
    Column() {
        TextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = modifier
        )

        Button(onClick = {
            preferences.set("key", secure.encryptAes(inputValue, key.getAesSecretKey()))


        }) {
            Text(text = "Set")
        }

        Button(onClick = {
            inputValue = secure.decryptAes(preferences.get("key"), key.getAesSecretKey())
        }) {
            Text(text = "Get")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Scuring_homework_otusTheme {

    }
}