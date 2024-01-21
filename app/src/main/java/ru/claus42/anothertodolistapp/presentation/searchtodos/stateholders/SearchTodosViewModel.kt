package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.AreBasicPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreDoneToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreImportantPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreLowPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreOnlyWithDeadlineToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDateSortTypeUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDateSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDeadlineSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetImportanceSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetSearchLayoutViewTypeUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetSearchLayoutViewTypeUseCase
import javax.inject.Inject


@FragmentScope
class SearchTodosViewModel @Inject constructor(
    getTodoItemListUseCase: GetTodoItemListUseCase,
    getSearchLayoutViewTypeUseCase: GetSearchLayoutViewTypeUseCase,
    private val setSearchLayoutViewTypeUseCase: SetSearchLayoutViewTypeUseCase,
    getDateSortTypeUseCase: GetDateSortTypeUseCase,
    getDateSortValueUseCase: GetDateSortValueUseCase,
    getDeadlineSortValueUseCase: GetDeadlineSortValueUseCase,
    getImportanceSortValueUseCase: GetImportanceSortValueUseCase,
    areLowPriorityToDosInSearchIncludedUseCase: AreLowPriorityToDosInSearchIncludedUseCase,
    areBasicPriorityToDosInSearchIncludedUseCase: AreBasicPriorityToDosInSearchIncludedUseCase,
    areImportantPriorityToDosInSearchIncludedUseCase: AreImportantPriorityToDosInSearchIncludedUseCase,
    areOnlyWithDeadlineToDosInSearchIncludedUseCase: AreOnlyWithDeadlineToDosInSearchIncludedUseCase,
    areDoneToDosInSearchIncludedUseCase: AreDoneToDosInSearchIncludedUseCase,
) : ViewModel() {

    val layoutViewType =
        getSearchLayoutViewTypeUseCase().asLiveData(viewModelScope.coroutineContext)

    private val areLowPriorityIncluded =
        areLowPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    private val areBasicPriorityIncluded =
        areBasicPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    private val areImportantPriorityIncluded =
        areImportantPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    private val areOnlyWithDeadlineIncluded =
        areOnlyWithDeadlineToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    private val areDoneIncluded =
        areDoneToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)

    private val dateSortType = getDateSortTypeUseCase().asLiveData(viewModelScope.coroutineContext)
    private val dateSortValue =
        getDateSortValueUseCase().asLiveData(viewModelScope.coroutineContext)
    private val deadlineSortValue =
        getDeadlineSortValueUseCase().asLiveData(viewModelScope.coroutineContext)
    private val importanceSortValue =
        getImportanceSortValueUseCase().asLiveData(viewModelScope.coroutineContext)

    private val searchingText: MutableLiveData<String> = MutableLiveData("")
    fun setSearchingText(text: String) {
        searchingText.postValue(text)
    }

    val todoItemsResult: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)

    private val todoItems = todoItemsResult.map { result ->
        if (result is DataResult.Success) {
            result.data
        } else {
            emptyList()
        }
    }

    private val searchResult: LiveData<List<TodoItemDomainEntity>> =
        searchingText.switchMap { text ->
            todoItems.map { items ->
                if (text.isNotBlank()) {
                    val result = items.filter { item ->
                        item.description.contains(text, true)
                    }
                    result
                } else {
                    emptyList()
                }
            }
        }

    val filteredAndSortedSearchResult = MediatorLiveData<List<TodoItemDomainEntity>>().apply {
        addSource(searchResult) { updateFilteredSortedTasks() }
        addSource(areLowPriorityIncluded) { updateFilteredSortedTasks() }
        addSource(areBasicPriorityIncluded) { updateFilteredSortedTasks() }
        addSource(areImportantPriorityIncluded) { updateFilteredSortedTasks() }
        addSource(areOnlyWithDeadlineIncluded) { updateFilteredSortedTasks() }
        addSource(areDoneIncluded) { updateFilteredSortedTasks() }
        addSource(dateSortType) { updateFilteredSortedTasks() }
        addSource(dateSortValue) { updateFilteredSortedTasks() }
        addSource(deadlineSortValue) { updateFilteredSortedTasks() }
        addSource(importanceSortValue) { updateFilteredSortedTasks() }
    }

    fun toggleLayoutManager() {
        layoutViewType.value?.let { layoutManagerType ->
            when (layoutManagerType) {
                SearchLayoutViewType.LINEAR -> setLayoutViewType(SearchLayoutViewType.GRID)
                SearchLayoutViewType.GRID -> setLayoutViewType(SearchLayoutViewType.LINEAR)
            }
        }
    }

    private fun setLayoutViewType(layoutType: SearchLayoutViewType) = viewModelScope.launch {
        setSearchLayoutViewTypeUseCase(layoutType)
    }

    private fun updateFilteredSortedTasks() {
        filteredAndSortedSearchResult.value = applyFiltersAndSort(
            searchResult.value ?: emptyList(),
            areLowPriorityIncluded.value ?: true,
            areBasicPriorityIncluded.value ?: true,
            areImportantPriorityIncluded.value ?: true,
            areOnlyWithDeadlineIncluded.value ?: false,
            areDoneIncluded.value ?: true,
            dateSortType.value ?: DateSortType.CREATION,
            dateSortValue.value ?: DateSortValue.NEW_ONES_FIRST,
            deadlineSortValue.value ?: DeadlineSortValue.NO_DEADLINE_SORT,
            importanceSortValue.value ?: ImportanceSortValue.NO_IMPORTANCE_SORT
        )
    }

    companion object {
        private fun TodoItemDomainEntity.combinedFilterPredicate(
            lowIncluded: Boolean,
            basicIncluded: Boolean,
            importantIncluded: Boolean,
            onlyWithDeadlineIncluded: Boolean,
            doneIncluded: Boolean,
        ): Boolean {
            return with(this) {
                val priorityPredicate = when (itemPriority) {
                    ItemPriority.LOW -> lowIncluded
                    ItemPriority.BASIC -> basicIncluded
                    ItemPriority.IMPORTANT -> importantIncluded
                }
                val deadlineOnlyPredicate = if (onlyWithDeadlineIncluded) {
                    isDeadlineEnabled
                } else true
                val donePredicate = if (!doneIncluded) !done else true

                priorityPredicate && deadlineOnlyPredicate && donePredicate
            }
        }

        private fun getDateComparator(
            dateSortType: DateSortType,
            dateSortValue: DateSortValue,
        ): Comparator<TodoItemDomainEntity> = Comparator { task1, task2 ->
            when (dateSortType) {
                DateSortType.CREATION -> when (dateSortValue) {
                    DateSortValue.NEW_ONES_FIRST -> task2.createdAt.compareTo(task1.createdAt)
                    DateSortValue.OLD_ONES_FIRST -> task1.createdAt.compareTo(task2.createdAt)
                }

                DateSortType.CHANGED -> when (dateSortValue) {
                    DateSortValue.NEW_ONES_FIRST -> task2.changedAt.compareTo(task1.changedAt)
                    DateSortValue.OLD_ONES_FIRST -> task1.changedAt.compareTo(task2.changedAt)
                }
            }
        }

        private fun getDeadlineComparator(deadlineSortValue: DeadlineSortValue)
                : Comparator<TodoItemDomainEntity> = Comparator { task1, task2 ->
            when (deadlineSortValue) {
                DeadlineSortValue.EARLIER_FIRST -> when {
                    task1.isDeadlineEnabled && task2.isDeadlineEnabled ->
                        task1.deadline.compareTo(task2.deadline)

                    task1.isDeadlineEnabled -> -1
                    task2.isDeadlineEnabled -> 1
                    else -> 0
                }

                DeadlineSortValue.LATER_FIRST -> when {
                    task1.isDeadlineEnabled && task2.isDeadlineEnabled ->
                        task2.deadline.compareTo(task1.deadline)

                    task1.isDeadlineEnabled -> -1
                    task2.isDeadlineEnabled -> 1
                    else -> 0
                }

                DeadlineSortValue.NO_DEADLINE_SORT -> 0
            }
        }


        private fun getImportanceComparator(importanceSortValue: ImportanceSortValue)
                : Comparator<TodoItemDomainEntity> = Comparator { task1, task2 ->
            when (importanceSortValue) {
                ImportanceSortValue.MOST_IMPORTANT_FIRST ->
                    task2.itemPriority.compareTo(task1.itemPriority)

                ImportanceSortValue.LESS_IMPORTANT_FIRST ->
                    task1.itemPriority.compareTo(task2.itemPriority)

                ImportanceSortValue.NO_IMPORTANCE_SORT -> 0
            }
        }

        private fun applyFiltersAndSort(
            list: List<TodoItemDomainEntity>,
            lowIncluded: Boolean,
            basicIncluded: Boolean,
            importantIncluded: Boolean,
            onlyWithDeadlineIncluded: Boolean,
            areDoneIncluded: Boolean,
            dateSortType: DateSortType,
            dateSortValue: DateSortValue,
            deadlineSortValue: DeadlineSortValue,
            importanceSortValue: ImportanceSortValue,
        ): List<TodoItemDomainEntity> {
            return list.filter {
                it.combinedFilterPredicate(
                    lowIncluded,
                    basicIncluded,
                    importantIncluded,
                    onlyWithDeadlineIncluded,
                    areDoneIncluded,
                )
            }.sortedWith(
                getImportanceComparator(importanceSortValue)
                    .thenComparing(getDeadlineComparator(deadlineSortValue))
                    .thenComparing(getDateComparator(dateSortType, dateSortValue))
            )
        }
    }
}
