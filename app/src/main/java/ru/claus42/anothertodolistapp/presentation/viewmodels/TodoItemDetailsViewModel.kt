package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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
    private val _events = MutableLiveData<UIEvent>()
    val events: LiveData<UIEvent> get() = _events

    private val todoItemId = MutableLiveData<UUID>()

    val todoItem: LiveData<DataResult<TodoItemDomainEntity>> =
        todoItemId.switchMap { id ->
            getItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
        }

    fun loadTodoItem(id: UUID) {
        todoItemId.value = id
    }

    fun updateTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch {
            updateTodoItemUseCase(item)
            _events.postValue(UIEvent.DataUpdated)
        }
    }

    fun deleteTodoItem(id: UUID) {
        viewModelScope.launch {
            deleteTodoItemUseCase(id)
            _events.postValue(UIEvent.DataUpdated)
        }
    }
}
