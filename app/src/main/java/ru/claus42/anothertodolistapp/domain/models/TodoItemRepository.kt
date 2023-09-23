package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity

interface TodoItemRepository {
    fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>>
}