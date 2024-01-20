package ru.claus42.anothertodolistapp.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import javax.inject.Named

@Module
class DataStoreModule {

    @AppScope
    @Provides
    @Named(USER_PREFERENCES)
    fun provideUserPreferencesDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @AppScope
    @Provides
    @Named(SORT_OPTIONS_PREFERENCES)
    fun provideSearchOptionsPreferencesDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(SORT_OPTIONS_PREFERENCES) }
        )
    }

    companion object {
        const val USER_PREFERENCES = "user_preferences"
        const val SORT_OPTIONS_PREFERENCES = "sort_options_preferences"
    }
}