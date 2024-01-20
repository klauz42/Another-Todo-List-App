package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType

interface SearchOptionsPreferencesRepository {
    fun getSearchLayoutViewType(): Flow<SearchLayoutViewType>
    suspend fun setSearchLayoutViewType(viewType: SearchLayoutViewType)

    fun getDateSortType(): Flow<DateSortType>
    suspend fun setDateSortType(sortType: DateSortType)
    fun getDateSortValue(): Flow<DateSortValue>
    suspend fun setDateSortValue(sortValue: DateSortValue)

    fun getDeadlineSortValue(): Flow<DeadlineSortValue>
    suspend fun setDeadlineSortValue(sortType: DeadlineSortValue)

    fun getImportanceSortValue(): Flow<ImportanceSortValue>
    suspend fun setImportanceSortValue(sortType: ImportanceSortValue)

    fun areLowPriorityTodosIncluded(): Flow<Boolean>
    suspend fun setLowPriorityTodosIncluded(lowIncluded: Boolean)

    fun areBasicPriorityTodosIncluded(): Flow<Boolean>
    suspend fun setBasicPriorityTodosIncluded(basicIncluded: Boolean)

    fun areImportantPriorityTodosIncluded(): Flow<Boolean>
    suspend fun setImportantPriorityTodosIncluded(importantIncluded: Boolean)

    fun areOnlyWithDeadlineIncluded(): Flow<Boolean>
    suspend fun setOnlyWithDeadlineIncluded(onlyWithDeadlineIncluded: Boolean)

    fun areDoneIncluded(): Flow<Boolean>
    suspend fun setDoneIncluded(doneIncluded: Boolean)
}