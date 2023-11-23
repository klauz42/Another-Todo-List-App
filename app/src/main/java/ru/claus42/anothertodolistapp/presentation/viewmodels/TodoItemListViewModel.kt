package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SwapItemsInsideListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemDoneStatusUseCase
import java.util.UUID
import javax.inject.Inject


class TodoItemListViewModel @Inject constructor(
    getTodoItemListUseCase: GetTodoItemListUseCase,
    private val updateTodoItemDoneStatusUseCase: UpdateTodoItemDoneStatusUseCase,
    private val swapItemsInsideListUseCase: SwapItemsInsideListUseCase
) : ViewModel() {
    private val _events = MutableLiveData<UIEvent>()
    val events: LiveData<UIEvent> get() = _events

    val todoItems: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)

    private var _lastSuccessfulData: List<TodoItemDomainEntity>? = null
    val lastSuccessfulData: List<TodoItemDomainEntity>? get() = _lastSuccessfulData

    init {
        todoItems.observeForever { result ->
            if (result is DataResult.Success) {
                _lastSuccessfulData = result.data
            }
        }
    }

    fun updateTodoItemDoneStatus(id: UUID, isDone: Boolean) {
        viewModelScope.launch {
            updateTodoItemDoneStatusUseCase(id, isDone)
        }
    }

    fun swapTodoItemsInsideList(fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            swapItemsInsideListUseCase(fromPosition, toPosition)
        }
    }
}