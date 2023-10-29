package ru.claus42.anothertodolistapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val todoItemsFlow =
        MutableStateFlow<DataResult<List<TodoItemDomainEntity>>>(DataResult.Loading)

    init {
        todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
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

    override fun updateTodoItem(updatedItem: TodoItemDomainEntity): Flow<DataResult<Nothing>> {
        localDataList.forEachIndexed { i, item ->
            if (updatedItem.id == item.id) {
                localDataList[i] = updatedItem.toLocalDataModel()
            }
        }
        todoItemsFlow.value = DataResult.Success(localDataList.map { it.toDomainModel() })
        return flowOf(DataResult.OK)
    }
}

