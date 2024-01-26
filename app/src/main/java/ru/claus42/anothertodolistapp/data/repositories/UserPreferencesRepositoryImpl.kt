package ru.claus42.anothertodolistapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import ru.claus42.anothertodolistapp.di.modules.DataStoreModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Named


@AppScope
class UserPreferencesRepositoryImpl @Inject constructor(
    @Named(DataStoreModule.USER_PREFERENCES) private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override fun isDoneTodosShown() = dataStore.data.map {
        it[IS_DONE_SHOWN] ?: true
    }

    override suspend fun setDoneTodosShown(isDone: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DONE_SHOWN] = isDone
        }
    }

    companion object {
        private val IS_DONE_SHOWN = booleanPreferencesKey("isDoneShown")
    }
}