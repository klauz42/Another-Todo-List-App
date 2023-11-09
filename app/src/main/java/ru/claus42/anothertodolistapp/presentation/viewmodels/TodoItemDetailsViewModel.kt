package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.DeleteTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemByIdUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemUseCase
import java.util.UUID
import javax.inject.Inject

class TodoItemDetailsViewModel @Inject constructor(
    private val getItemUseCase: GetTodoItemByIdUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase
) : ViewModel() {

    private val todoItemId = MutableLiveData<UUID>()

    val todoItem: LiveData<DataResult<TodoItemDomainEntity>> =
        todoItemId.switchMap { id ->
            getItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
        }

    fun loadTodoItem(id: UUID) {
        todoItemId.value = id
    }

    fun updateTodoItem(item: TodoItemDomainEntity): LiveData<DataResult<Nothing>> {
        return updateTodoItemUseCase(item).asLiveData(viewModelScope.coroutineContext)
    }

    fun deleteTodoItem(id: UUID) : LiveData<DataResult<Nothing>> {
        return deleteTodoItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
    }
}
