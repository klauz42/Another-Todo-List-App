package ru.claus42.anothertodolistapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import ru.claus42.anothertodolistapp.di.modules.DataStoreModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import javax.inject.Inject
import javax.inject.Named

@AppScope
class SearchOptionsPreferencesRepositoryImpl @Inject constructor(
    @Named(DataStoreModule.SORT_OPTIONS_PREFERENCES) private val dataStore: DataStore<Preferences>
) : SearchOptionsPreferencesRepository {

    private companion object {
        val LAYOUT_VIEW_TYPE = stringPreferencesKey("layoutViewType")
        val DATA_SORT_TYPE = stringPreferencesKey("dateSortType")
        val DATA_SORT_VALUE = stringPreferencesKey("dateSortValue")
        val DEADLINE_SORT = stringPreferencesKey("deadlineSort")
        val IMPORTANCE_SORT = stringPreferencesKey("importanceSort")
        val LOW_PRIORITY_INCLUDED = booleanPreferencesKey("lowIncluded")
        val BASIC_PRIORITY_INCLUDED = booleanPreferencesKey("basicIncluded")
        val IMPORTANT_PRIORITY_INCLUDED = booleanPreferencesKey("importantIncluded")
        val ONLY_WITH_DEADLINE_INCLUDED = booleanPreferencesKey("onlyWithDeadline")
        val DONE_INCLUDED = booleanPreferencesKey("done")

        const val TAG = "SearchOptionsPreferencesRepositoryImpl"
    }

    override fun getSearchLayoutViewType() = dataStore.data.map { preferences ->
        SearchLayoutViewType.valueOf(
            preferences[LAYOUT_VIEW_TYPE] ?: SearchLayoutViewType.LINEAR.name
        )
    }

    override suspend fun setSearchLayoutViewType(viewType: SearchLayoutViewType) {
        dataStore.edit { preferences ->
            preferences[LAYOUT_VIEW_TYPE] = viewType.name
        }
    }

    override fun getDateSortType() = dataStore.data.map { preferences ->
        DateSortType.valueOf(
            preferences[DATA_SORT_TYPE] ?: DateSortType.CREATION.name
        )
    }

    override suspend fun setDateSortType(sortType: DateSortType) {
        dataStore.edit { preferences ->
            preferences[DATA_SORT_TYPE] = sortType.name
        }
    }

    override fun getDateSortValue() = dataStore.data.map { preferences ->
        DateSortValue.valueOf(
            preferences[DATA_SORT_VALUE] ?: DateSortValue.NEW_ONES_FIRST.name
        )
    }

    override suspend fun setDateSortValue(sortValue: DateSortValue) {
        dataStore.edit { preferences ->
            preferences[DATA_SORT_VALUE] = sortValue.name
        }
    }

    override fun getDeadlineSortValue() = dataStore.data.map { preferences ->
        DeadlineSortValue.valueOf(
            preferences[DEADLINE_SORT] ?: DeadlineSortValue.NO_DEADLINE_SORT.name
        )
    }

    override suspend fun setDeadlineSortValue(sortType: DeadlineSortValue) {
        dataStore.edit { preferences ->
            preferences[DEADLINE_SORT] = sortType.name
        }
    }

    override fun getImportanceSortValue() = dataStore.data.map { preferences ->
        ImportanceSortValue.valueOf(
            preferences[IMPORTANCE_SORT] ?: ImportanceSortValue.NO_IMPORTANCE_SORT.name
        )
    }

    override suspend fun setImportanceSortValue(sortType: ImportanceSortValue) {
        dataStore.edit { preferences ->
            preferences[IMPORTANCE_SORT] = sortType.name
        }
    }

    override fun areLowPriorityTodosIncluded() = dataStore.data.map { preferences ->
        preferences[LOW_PRIORITY_INCLUDED] ?: true
    }

    override suspend fun setLowPriorityTodosIncluded(lowIncluded: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOW_PRIORITY_INCLUDED] = lowIncluded
        }
    }

    override fun areBasicPriorityTodosIncluded() = dataStore.data.map { preferences ->
        preferences[BASIC_PRIORITY_INCLUDED] ?: true
    }

    override suspend fun setBasicPriorityTodosIncluded(basicIncluded: Boolean) {
        dataStore.edit { preferences ->
            preferences[BASIC_PRIORITY_INCLUDED] = basicIncluded
        }
    }

    override fun areImportantPriorityTodosIncluded() = dataStore.data.map { preferences ->
        preferences[IMPORTANT_PRIORITY_INCLUDED] ?: true
    }

    override suspend fun setImportantPriorityTodosIncluded(importantIncluded: Boolean) {
        dataStore.edit { preferences ->
            preferences[IMPORTANT_PRIORITY_INCLUDED] = importantIncluded
        }
    }

    override fun areOnlyWithDeadlineIncluded() = dataStore.data.map { preferences ->
        preferences[ONLY_WITH_DEADLINE_INCLUDED] ?: false
    }

    override suspend fun setOnlyWithDeadlineIncluded(onlyWithDeadlineIncluded: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONLY_WITH_DEADLINE_INCLUDED] = onlyWithDeadlineIncluded
        }
    }

    override fun areDoneIncluded() = dataStore.data.map { preferences ->
        preferences[DONE_INCLUDED] ?: false
    }

    override suspend fun setDoneIncluded(doneIncluded: Boolean) {
        dataStore.edit { preferences ->
            preferences[DONE_INCLUDED] = doneIncluded
        }
    }
}