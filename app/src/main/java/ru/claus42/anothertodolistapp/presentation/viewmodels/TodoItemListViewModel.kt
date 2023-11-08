package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemDoneStatusUseCase
import java.util.UUID
import javax.inject.Inject

class TodoItemListViewModel @Inject constructor(
    getTodoItemListUseCase: GetTodoItemListUseCase,
    private val updateTodoItemDoneStatusUseCase: UpdateTodoItemDoneStatusUseCase
) : ViewModel() {
    val todoItems: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)

    fun updateTodoItemDoneStatus(id: UUID, isDone: Boolean): LiveData<DataResult<Nothing>> {
        return updateTodoItemDoneStatusUseCase(id, isDone)
            .asLiveData(viewModelScope.coroutineContext)
    }
}