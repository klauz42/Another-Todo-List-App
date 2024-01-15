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
import java.time.ZoneId
import java.time.ZonedDateTime
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

    private val repoException = MutableLiveData<Exception>()

    private var initialItem: TodoItemDomainEntity? = null

    val isItemChanged
        get() = !(_todoItem.value?.equalsByContent(initialItem) ?: (initialItem == null))

    val todoItemResult: LiveData<DataResult<TodoItemDomainEntity>> =
        MediatorLiveData<DataResult<TodoItemDomainEntity>>().apply {
            addSource(todoItemIdLiveData.switchMap { id ->
                getItemUseCase(id).asLiveData(viewModelScope.coroutineContext)
            }) {
                value = it
            }
            addSource(repoException) { exception ->
                value = DataResult.Error(exception)
            }
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
            try {
                updateTodoItemUseCase(item)
            } catch (e: Exception) {
                repoException.postValue(e)
            }
        }
    }

    fun deleteTodoItem() {
        _todoItem.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    deleteTodoItemUseCase(it)
                } catch (e: Exception) {
                    repoException.postValue(e)
                }
            }
        }
    }

    fun setPriority(newPriority: ItemPriority) {
        if (_todoItem.value?.itemPriority != newPriority) {
            _todoItem.value = _todoItem.value?.copy(
                itemPriority = newPriority,
                changedAt = ZonedDateTime.now()
            )
        }
    }

    fun updateDescription(newDescription: String) {
        if (_todoItem.value?.description != newDescription) {
            _todoItem.value = _todoItem.value?.copy(
                description = newDescription,
                changedAt = ZonedDateTime.now()
            )
        }
    }

    fun getDescription(): String? {
        return _todoItem.value?.description
    }

    fun updateDeadlineIsEnabled(isEnabled: Boolean) {
        _todoItem.value = _todoItem.value?.copy(
            isDeadlineEnabled = isEnabled,
            changedAt = ZonedDateTime.now()
        )
    }

    fun getDeadline(): ZonedDateTime? {
        return _todoItem.value?.deadline
    }

    fun updateDeadline(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
        _todoItem.value?.let {
            val newDeadline = ZonedDateTime.of(
                year, month, dayOfMonth, hourOfDay, minute, 0, 0, ZoneId.systemDefault()
            )
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = ZonedDateTime.now()
            )
        }
    }

    fun updateDeadlineDate(year: Int, month: Int, dayOfMonth: Int) {
        _todoItem.value?.let {
            val hourOfDay = it.deadline.hour
            val minute = it.deadline.minute

            val newDeadline = ZonedDateTime.of(
                year, month, dayOfMonth, hourOfDay, minute, 0, 0, ZoneId.systemDefault()
            )
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = ZonedDateTime.now()
            )
        }
    }

    fun updateDeadlineTime(hourOfDay: Int, minute: Int) {
        _todoItem.value?.let {
            val year = it.deadline.year
            val month = it.deadline.monthValue
            val dayOfMonth = it.deadline.dayOfMonth

            val newDeadline = ZonedDateTime.of(
                year, month, dayOfMonth, hourOfDay, minute, 0, 0, ZoneId.systemDefault()
            )
            _todoItem.value = _todoItem.value?.copy(
                deadline = newDeadline,
                changedAt = ZonedDateTime.now()
            )
        }
    }


    var isTextEditScrolledDown = false
    var isNewItem = false
    var descriptionCursorPosition: Int? = null
}
