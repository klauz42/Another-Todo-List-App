package ru.claus42.anothertodolistapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
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

    override suspend fun getUserId(): String {
        val values = dataStore.data.first()

        return values[USER_ID] ?: ""
    }

    override suspend fun setUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = id
        }
    }

    companion object {
        private val USER_ID = stringPreferencesKey("userId")
        private val IS_DONE_SHOWN = booleanPreferencesKey("isDoneShown")
        private val IS_GRID_SEARCH_LAYOUT = booleanPreferencesKey("isGridSearchLayout")
    }
}