package ru.claus42.anothertodolistapp.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.di.scopes.AppScope

private const val USER_PREFERENCES = "user_preferences"

@Module
class DataStoreModule {

    @AppScope
    @Provides
    fun providePreferenceDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}