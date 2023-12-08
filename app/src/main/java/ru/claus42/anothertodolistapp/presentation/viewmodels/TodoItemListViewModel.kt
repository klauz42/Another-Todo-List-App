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
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.MoveItemInListUseCase
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
    private val undoTodoItemDeletingUseCase: UndoTodoItemDeletingUseCase
) : ViewModel() {
    private val _showDone = MutableLiveData<Boolean>(true)
    val showDone: MutableLiveData<Boolean> = _showDone

    fun toggleShowDone() {
        _showDone.value = _showDone.value != true
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

    fun updateTodoItemDoneStatus(id: UUID, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoItemDoneStatusUseCase(id, isDone)
        }
    }

    fun moveTodoItemsInList(fromId: UUID, toId: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            moveItemInListUseCase(fromId, toId)
        }
    }

    fun deleteTodoItem(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTodoItemUseCase(id)
        }
    }

    fun undoTodoItemDeletion() {
        viewModelScope.launch(Dispatchers.IO) {
            undoTodoItemDeletingUseCase()
        }
    }
}