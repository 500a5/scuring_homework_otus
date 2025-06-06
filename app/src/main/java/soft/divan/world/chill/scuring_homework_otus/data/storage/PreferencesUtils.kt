package soft.divan.world.chill.scuring_homework_otus.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import soft.divan.world.chill.scuring_homework_otus.data.encryption.DataStoreEncryption
import javax.inject.Inject

private const val sharedPrefsFile: String = "securePref"

class PreferencesUtils
@Inject constructor(
    private val applicationContext: Context,
    private val dataStoreEncryption: DataStoreEncryption
) {

    val stringDataFlow: Flow<String?>
        get() = applicationContext.dataStore.data.map { preferences ->
            preferences[STRING_DATA]?.let { dataStoreEncryption.decryptData(it) }
        }

    suspend fun setString(value: String) {
           applicationContext.dataStore.edit { preferences ->
               val encryptedValue = dataStoreEncryption.encryptData(value)
               encryptedValue.let { preferences[STRING_DATA] = it }
            }
    }

    val booleanDataFlow: Flow<Boolean?>
        get() = applicationContext.dataStore.data.map { preferences ->
            preferences[BOOLEAN_DATA]?.let { dataStoreEncryption.decryptData(it).toBooleanStrictOrNull() }
        }


    suspend fun setBoolean( value: Boolean) {
        applicationContext.dataStore.edit { preferences ->
            val encryptedValue = dataStoreEncryption.encryptData(value.toString())
            preferences[BOOLEAN_DATA] = encryptedValue
        }
    }



    companion object {
        private val Context.dataStore by preferencesDataStore(name = sharedPrefsFile)
        private val STRING_DATA = stringPreferencesKey("key_string_data")
        private val BOOLEAN_DATA = stringPreferencesKey("key_boolean_data")
    }
}