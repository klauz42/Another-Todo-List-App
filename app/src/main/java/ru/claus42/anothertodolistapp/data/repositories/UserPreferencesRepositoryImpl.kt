package ru.claus42.anothertodolistapp.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository
import javax.inject.Inject

private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES
)

@AppScope
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
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
    }
}