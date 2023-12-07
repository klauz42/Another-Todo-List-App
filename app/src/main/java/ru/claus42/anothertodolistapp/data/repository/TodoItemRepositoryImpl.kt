package ru.claus42.anothertodolistapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.data.local.models.mappers.toLocalDataModel
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.stub_stuff.stubTodoItemEntityList
import java.util.UUID
import javax.inject.Inject

@AppScope
class TodoItemRepositoryImpl @Inject constructor() : TodoItemRepository {
    private val localDataList = stubTodoItemEntityList.toMutableList()
    private val _todoItemsFlow =
        MutableStateFlow<DataResult<List<TodoItemDomainEntity>>>(DataResult.Loading)
    val todoItemsFlow: StateFlow<DataResult<List<TodoItemDomainEntity>>> = _todoItemsFlow

    private var lastDeleted: TodoItemLocalDataEntity? = null
    private var lastDeletedPosition: Int? = null

    init {
        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    override fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>> = todoItemsFlow

    override fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>> {
        return flowOf(DataResult.on {
            var localDataItem: TodoItemLocalDataEntity? = null
            for (item in localDataList) {
                if (item.id == id)
                    localDataItem = item
            }

            if (localDataItem == null) {
                throw NoSuchElementException("No such item with uuid=$id")
            }

            localDataItem.toDomainModel()
        }
        )
    }

    override fun updateTodoItem(newItem: TodoItemDomainEntity) {
        localDataList.forEachIndexed { i, item ->
            if (newItem.id == item.id) {
                localDataList[i] = newItem.toLocalDataModel()
            }
        }
        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    override fun updateDoneStatus(id: UUID, isDone: Boolean) {
        localDataList.forEachIndexed { i, item ->
            if (id == item.id) localDataList[i].apply { done = isDone }
        }
        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    override fun deleteItem(id: UUID) {
        localDataList.forEachIndexed { i, item ->
            if (item.id == id) {
                lastDeleted = item
                lastDeletedPosition = i
            }
        }

        localDataList.remove(localDataList.find { it.id == id })

        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    override fun moveItem(from: Int, to: Int) {
        val movingItem = localDataList[from]
        localDataList.removeAt(from)
        localDataList.add(to, movingItem)

        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    private fun addItem(position: Int, item: TodoItemDomainEntity) {
        localDataList.add(position, item.toLocalDataModel())
        _todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
    }

    override fun undoDeletion() {
        lastDeleted?.let { item ->
            lastDeletedPosition?.let { pos ->
                addItem(pos, item.toDomainModel())
            }
        }
    }
}

