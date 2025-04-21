package soft.divan.world.chill.scuring_homework_otus

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.MasterKey
import soft.divan.world.chill.scuring_homework_otus.storage.PreferencesUtils
import soft.divan.world.chill.scuring_homework_otus.crypto.Keys
import soft.divan.world.chill.scuring_homework_otus.crypto.Security
import soft.divan.world.chill.scuring_homework_otus.ui.theme.Scuring_homework_otusTheme

class MainActivity : FragmentActivity() {


    @RequiresApi(Build.VERSION_CODES.P)
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
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "auth",

                        ) {
                        composable("auth") {
                            AuthenticationScreen(
                                preferences = preferences,
                                onAuthSuccess = {
                                    navController.navigate("main_screen") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("main_screen") {
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
    }
}