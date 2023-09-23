package ru.claus42.anothertodolistapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import ru.claus42.anothertodolistapp.stub_stuff.stubTodoItemEntityList
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(): TodoItemRepository {
    override fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>> {
        val domainEntityList = stubTodoItemEntityList.map { it.toDomainModel() }
        return flowOf(DataResult.on {  domainEntityList })
    }
}

