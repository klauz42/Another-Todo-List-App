package ru.claus42.anothertodolistapp.presentation.todoitemlist.stateholders

import androidx.lifecycle.LiveData
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

    private val _todoItems: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)

    val todoItems: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        _showDone.switchMap { showDone ->
            _todoItems.map { result ->
                when (result) {
                    is DataResult.Success -> {
                        if (!showDone) {
                            DataResult.Success(result.data.filter { !it.done })
                        } else {
                            result
                        }
                    }

                    else -> result
                }
            }
        }

    val countDoneLiveData: LiveData<Int?> =
        _todoItems.map {
            if (it is DataResult.Success) {
                it.data.count { entity ->
                    !entity.done
                }
            } else {
                null
            }
        }

    fun updateTodoItemDoneStatus(item: TodoItemDomainEntity, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemDoneStatusUseCase(item, isDone)
        }
    }

    fun moveTodoItemsInList(fromId: UUID, toId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            moveItemInListUseCase(fromId, toId)
        }
    }

    fun deleteTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTodoItemUseCase(item)
        }
    }

    fun undoTodoItemDeletion() {
        viewModelScope.launch(Dispatchers.IO) {
            undoTodoItemDeletingUseCase()
        }
    }

    fun addTodoItem(item: TodoItemDomainEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            addTodoItemUseCase(item)
        }
    }
}