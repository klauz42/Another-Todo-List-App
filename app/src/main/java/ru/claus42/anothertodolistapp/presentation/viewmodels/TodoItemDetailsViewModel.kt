package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.DeleteTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemByIdUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemUseCase
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemDetailsViewModel @Inject constructor(
    private val getItemUseCase: GetTodoItemByIdUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase
) : ViewModel() {

    private val todoItemIdLiveData = MutableLiveData<UUID>()
    val todoId get() = todoItemIdLiveData.value

    private var initialItem: TodoItemDomainEntity? = null

    val isItemChanged get() = (todoItem.value != initialItem)

    val todoItemResult: LiveData<DataResult<TodoItemDomainEntity>> =
        todoItemIdLiveData.switchMap { id ->
            getItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
        }

    val todoItem: LiveData<TodoItemDomainEntity?> = todoItemResult.map { result ->
        if (result is DataResult.Success) {
            initialItem = initialItem ?: result.data.copy()
            result.data
        } else {
            null
        }
    }

    fun initialize(id: UUID, isNewItem: Boolean) {
        if (initialItem == null) {
            loadTodoItem(this, id)
            this.isNewItem = isNewItem
        }
    }

    fun saveChanges() {
        todoItem.value?.let {
            initialItem = it.copy()
            updateTodoItem(it)
        }

        isNewItem = false
    }

    private fun updateTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemUseCase(item)
        }
    }

    fun deleteTodoItem() {
        todoId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                deleteTodoItemUseCase(it)
            }
        }
    }

    var isTextEditScrolledDown = false
    var isNewItem = false
    var descriptionCursorPosition: Int? = null

    companion object {
        fun loadTodoItem(todoItemDetailsViewModel: TodoItemDetailsViewModel, id: UUID) {
            todoItemDetailsViewModel.todoItemIdLiveData.value = id
        }
    }
}
