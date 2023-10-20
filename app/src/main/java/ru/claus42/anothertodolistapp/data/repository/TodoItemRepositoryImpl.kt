package ru.claus42.anothertodolistapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.stub_stuff.stubTodoItemEntityList
import java.util.UUID
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor() : TodoItemRepository {
    private val localDataList = stubTodoItemEntityList

    override fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>> {
        val domainEntityList = localDataList.map { it.toDomainModel() }
        return flowOf(DataResult.on { domainEntityList })
    }

    override fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>> {
        var localDataItem: TodoItemLocalDataEntity? = null
        for (item in localDataList) {
            if (item.id == id)
                localDataItem = item
        }

        if (localDataItem == null) {
            return flowOf(
                DataResult.Error(
                    NoSuchElementException(
                        "No such item with uuid=$id"
                    )
                )
            )
        }

        return flowOf(DataResult.on { localDataItem.toDomainModel() })
    }
}

