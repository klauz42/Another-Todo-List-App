package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import ru.claus42.anothertodolistapp.domain.usecases.GetDateSortTypeUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDateSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDeadlineSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetImportanceSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetDateSortTypeUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetDateSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetDeadlineSortValueUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetImportanceSortUseCase
import javax.inject.Inject


@FragmentScope
class SortOptionsViewModel @Inject constructor(
    private val getDateSortTypeUseCase: GetDateSortTypeUseCase,
    private val setDateSortTypeUseCase: SetDateSortTypeUseCase,
    private val getDateSortValueUseCase: GetDateSortValueUseCase,
    private val setDateSortValueUseCase: SetDateSortValueUseCase,
    private val getDeadlineSortValueUseCase: GetDeadlineSortValueUseCase,
    private val setDeadlineSortValueUseCase: SetDeadlineSortValueUseCase,
    private val getImportanceSortValueUseCase: GetImportanceSortValueUseCase,
    private val setImportanceSortUseCase: SetImportanceSortUseCase,
) : ViewModel() {

    val dateSortType = getDateSortTypeUseCase().asLiveData(viewModelScope.coroutineContext)
    val dateSortValue =
        getDateSortValueUseCase().asLiveData(viewModelScope.coroutineContext)
    val deadlineSortValue =
        getDeadlineSortValueUseCase().asLiveData(viewModelScope.coroutineContext)
    val importanceSortValue =
        getImportanceSortValueUseCase().asLiveData(viewModelScope.coroutineContext)

    fun setDateSortType(sortType: DateSortType) = viewModelScope.launch(Dispatchers.IO) {
        setDateSortTypeUseCase(sortType)
    }

    fun setDateSortValue(sortValue: DateSortValue) = viewModelScope.launch(Dispatchers.IO) {
        setDateSortValueUseCase(sortValue)
    }

    fun setDeadlineSortValue(sortValue: DeadlineSortValue) = viewModelScope.launch {
        setDeadlineSortValueUseCase(sortValue)
    }

    fun setImportanceSortValue(sortValue: ImportanceSortValue) =
        viewModelScope.launch(Dispatchers.IO) {
            setImportanceSortUseCase(sortValue)
        }
}
