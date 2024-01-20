package ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.usecases.AreBasicPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreDoneToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreImportantPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreLowPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.AreOnlyWithDeadlineToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetBasicPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetDoneToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetImportantPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetLowPriorityToDosInSearchIncludedUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetOnlyWithDeadlineToDosInSearchIncludedUseCase
import javax.inject.Inject


@FragmentScope
class FilterOptionsViewModel @Inject constructor(
    areLowPriorityToDosInSearchIncludedUseCase: AreLowPriorityToDosInSearchIncludedUseCase,
    areBasicPriorityToDosInSearchIncludedUseCase: AreBasicPriorityToDosInSearchIncludedUseCase,
    areImportantPriorityToDosInSearchIncludedUseCase: AreImportantPriorityToDosInSearchIncludedUseCase,
    areOnlyWithDeadlineToDosInSearchIncludedUseCase: AreOnlyWithDeadlineToDosInSearchIncludedUseCase,
    areDoneToDosInSearchIncludedUseCase: AreDoneToDosInSearchIncludedUseCase,
    private val setLowPriorityToDosInSearchIncludedUseCase: SetLowPriorityToDosInSearchIncludedUseCase,
    private val setBasicPriorityToDosInSearchIncludedUseCase: SetBasicPriorityToDosInSearchIncludedUseCase,
    private val setImportantPriorityToDosInSearchIncludedUseCase: SetImportantPriorityToDosInSearchIncludedUseCase,
    private val setOnlyWithDeadlineToDosInSearchIncludedUseCase: SetOnlyWithDeadlineToDosInSearchIncludedUseCase,
    private val setDoneToDosInSearchIncludedUseCase: SetDoneToDosInSearchIncludedUseCase,
) : ViewModel() {

    val areLowPriorityIncluded =
        areLowPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    val areBasicPriorityIncluded =
        areBasicPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    val areImportantPriorityIncluded =
        areImportantPriorityToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    val areOnlyWithDeadlineIncluded =
        areOnlyWithDeadlineToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)
    val areDoneIncluded =
        areDoneToDosInSearchIncludedUseCase().asLiveData(viewModelScope.coroutineContext)

    fun setLowPriorityToDosIncluded(included: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        setLowPriorityToDosInSearchIncludedUseCase(included)
    }

    fun setBasicPriorityToDosIncluded(included: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        setBasicPriorityToDosInSearchIncludedUseCase(included)
    }

    fun setImportantPriorityToDosIncluded(included: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            setImportantPriorityToDosInSearchIncludedUseCase(included)
        }

    fun setOnlyWithDeadlineToDosIncluded(included: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            setOnlyWithDeadlineToDosInSearchIncludedUseCase(included)
        }

    fun setDoneToDosIncluded(included: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        setDoneToDosInSearchIncludedUseCase(included)
    }
}
