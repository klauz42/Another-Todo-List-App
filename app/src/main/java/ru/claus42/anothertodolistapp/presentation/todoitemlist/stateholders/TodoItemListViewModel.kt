package ru.claus42.anothertodolistapp.presentation.todoitemlist.stateholders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.AddTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.DeleteTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetDoneTodosShownUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.MoveItemInListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.SetDoneTodosShownUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UndoTodoItemDeletingUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemDoneStatusUseCase
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemListViewModel @Inject constructor(
    getTodoItemListUseCase: GetTodoItemListUseCase,
    private val updateTodoItemDoneStatusUseCase: UpdateTodoItemDoneStatusUseCase,
    private val moveItemInListUseCase: MoveItemInListUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val undoTodoItemDeletingUseCase: UndoTodoItemDeletingUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    getDoneTodosShownUseCase: GetDoneTodosShownUseCase,
    private val setDoneTodosShownUseCase: SetDoneTodosShownUseCase,
) : ViewModel() {

    private val _showDone: LiveData<Boolean> =
        getDoneTodosShownUseCase().asLiveData(viewModelScope.coroutineContext)
    val showDone: LiveData<Boolean> = _showDone

    fun toggleShowDone() {
        val newValue = _showDone.value != true
        viewModelScope.launch(Dispatchers.IO) {
            setDoneTodosShownUseCase(newValue)
        }
    }

    private val repoException = MutableLiveData<Exception>()
    fun clearError() {
        _todoItemsResult.postValue(DataResult.OK)
    }

    private val _todoItemsResult: MutableLiveData<DataResult<List<TodoItemDomainEntity>>> =
        MediatorLiveData<DataResult<List<TodoItemDomainEntity>>>().apply {
            addSource(getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)) {
                value = it
            }
            addSource(repoException.distinctUntilChanged()) { exception ->
                value = DataResult.Error(exception)
            }
        }
    val todoItemsResult: LiveData<DataResult<List<TodoItemDomainEntity>>> get() = _todoItemsResult


    private val _todoItems = MediatorLiveData<List<TodoItemDomainEntity>>().apply {
        addSource(_todoItemsResult) { result ->
            if (result is DataResult.Success) {
                value = result.data.map { it.copy() }
            }
        }
    }

    val todoItems: LiveData<List<TodoItemDomainEntity>> =
        _showDone.switchMap { showDone ->
            _todoItems.map { items ->
                if (!showDone) {
                    items.filter { !it.done }
                } else {
                    items
                }
            }
        }


    val countDoneLiveData: LiveData<Int?> =
        _todoItems.map {
            it.count { entity ->
                !entity.done
            }
        }


    fun updateTodoItemDoneStatus(id: UUID, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            interactWithRepo { updateTodoItemDoneStatusUseCase(id, isDone) }
        }
    }

    fun moveTodoItemsInList(fromId: UUID, toId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            interactWithRepo { moveItemInListUseCase(fromId, toId) }
        }
    }

    fun deleteTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            interactWithRepo { deleteTodoItemUseCase(item) }
        }
    }

    fun undoTodoItemDeletion() {
        viewModelScope.launch(Dispatchers.IO) {
            interactWithRepo { undoTodoItemDeletingUseCase() }
        }
    }

    fun addTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            interactWithRepo { addTodoItemUseCase(item) }
        }
    }

    private suspend fun interactWithRepo(f: suspend () -> Unit) {
        try {
            f()
        } catch (e: Exception) {
            Log.e(TAG, "interactWithRepo: ${e.message}")
            repoException.postValue(e)
        }
    }

    private companion object {
        const val TAG = "TodoItemListViewModel"
    }
}