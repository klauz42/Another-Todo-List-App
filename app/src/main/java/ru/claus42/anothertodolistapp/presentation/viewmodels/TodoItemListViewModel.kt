package ru.claus42.anothertodolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.domain.usecases.DeleteTodoItemUseCase
import ru.claus42.anothertodolistapp.domain.usecases.GetTodoItemListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.MoveItemInsideListUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UndoTodoItemDeletingUseCase
import ru.claus42.anothertodolistapp.domain.usecases.UpdateTodoItemDoneStatusUseCase
import java.util.UUID
import javax.inject.Inject


@FragmentScope
class TodoItemListViewModel @Inject constructor(
    getTodoItemListUseCase: GetTodoItemListUseCase,
    private val updateTodoItemDoneStatusUseCase: UpdateTodoItemDoneStatusUseCase,
    private val moveItemInsideListUseCase: MoveItemInsideListUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val undoTodoItemDeletingUseCase: UndoTodoItemDeletingUseCase
) : ViewModel() {
    val todoItems: LiveData<DataResult<List<TodoItemDomainEntity>>> =
        getTodoItemListUseCase().asLiveData(viewModelScope.coroutineContext)

    val countDoneLiveData: LiveData<Int?> =
        todoItems.map {
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

    fun moveTodoItemsInsideList(fromPosition: Int, toPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            moveItemInsideListUseCase(fromPosition, toPosition)
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