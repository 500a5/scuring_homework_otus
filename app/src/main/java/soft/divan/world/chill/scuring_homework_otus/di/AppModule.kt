package soft.divan.world.chill.scuring_homework_otus.di

import android.content.Context
import android.os.Build
import soft.divan.world.chill.scuring_homework_otus.data.encryption.KeyManagerLowerThanM
import soft.divan.world.chill.scuring_homework_otus.data.encryption.KeyManagerMAndHigher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import soft.divan.world.chill.scuring_homework_otus.data.encryption.DataStoreEncryption
import soft.divan.world.chill.scuring_homework_otus.data.storage.PreferencesUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDataStoreEncryption(
        @ApplicationContext context: Context
    ): DataStoreEncryption {
        val keyManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyManagerMAndHigher()
        } else {
            KeyManagerLowerThanM(context)
        }
        return DataStoreEncryption(keyManager)
    }

    @Singleton
    @Provides
    fun provideUserPreferences(
        @ApplicationContext context: Context,
        dataStoreEncryption: DataStoreEncryption
    ): PreferencesUtils {
        return PreferencesUtils(context, dataStoreEncryption)
    }


}