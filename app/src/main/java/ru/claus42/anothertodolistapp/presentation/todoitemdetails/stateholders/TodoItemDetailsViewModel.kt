package ru.claus42.anothertodolistapp.presentation.todoitemdetails.stateholders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.DeleteTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemByIdUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemUseCase
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemDetailsViewModel @Inject constructor(
    private val getItemUseCase: GetTodoItemByIdUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : ViewModel() {

    private val todoItemIdLiveData = MutableLiveData<UUID>()
    val todoId get() = todoItemIdLiveData.value

    private var initialItem: TodoItemDomainEntity? = null

    val isItemChanged
        get() = !(_todoItem.value?.equalsByContent(initialItem) ?: (initialItem == null))
    val todoItemResult: LiveData<DataResult<TodoItemDomainEntity>> =
        todoItemIdLiveData.switchMap { id ->
            getItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
        }

    private val _todoItem = MediatorLiveData<TodoItemDomainEntity?>()
    val todoItem: LiveData<TodoItemDomainEntity?> = _todoItem

    init {
        _todoItem.addSource(todoItemResult) { result ->
            if (result is DataResult.Success) {
                initialItem = initialItem ?: result.data.copy()
                _todoItem.value = result.data.copy()
            }
        }
    }

    private fun loadTodoItem(id: UUID) {
        todoItemIdLiveData.value = id
    }

    fun initialize(id: UUID, isNewItem: Boolean) {
        if (initialItem == null) {
            loadTodoItem(id)
            this.isNewItem = isNewItem
        }
    }

    fun saveChanges() {
        _todoItem.value?.let {
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
        _todoItem.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                deleteTodoItemUseCase(it)
            }
        }
    }

    fun setPriority(newPriority: ItemPriority) {
        if (_todoItem.value?.itemPriority != newPriority) {
            _todoItem.value = _todoItem.value?.copy(
                itemPriority = newPriority,
                changedAt = LocalDateTime.now()
            )
        }
    }

    fun updateDescription(newDescription: String) {
        if (_todoItem.value?.description != newDescription) {
            _todoItem.value = _todoItem.value?.copy(
                description = newDescription,
                changedAt = LocalDateTime.now()
            )
        }
    }

    fun getDescription(): String? {
        return _todoItem.value?.description
    }

    fun updateDeadlineIsEnabled(isEnabled: Boolean) {
        _todoItem.value = _todoItem.value?.copy(
            isDeadlineEnabled = isEnabled,
            changedAt = LocalDateTime.now()
        )
    }

    fun getDeadline(): LocalDateTime? {
        return _todoItem.value?.deadline
    }

    fun updateDeadline(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        _todoItem.value?.let {
            val newDeadline = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute)
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = LocalDateTime.now()
            )
        }
    }

    fun updateDeadlineDate(year: Int, month: Int, dayOfMonth: Int) {
        _todoItem.value?.let {
            val hourOfDay = it.deadline.hour
            val minute = it.deadline.minute

            val newDeadline = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute)
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = LocalDateTime.now()
            )
        }
    }

    fun updateDeadlineTime(hourOfDay: Int, minute: Int) {
        _todoItem.value?.let {
            val year = it.deadline.year
            val month = it.deadline.month
            val dayOfMonth = it.deadline.dayOfMonth

            val newDeadline = LocalDateTime.of(year, month, dayOfMonth, hourOfDay, minute)
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = LocalDateTime.now()
            )
        }
    }

    var isTextEditScrolledDown = false
    var isNewItem = false
    var descriptionCursorPosition: Int? = null
}
