package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID

interface TodoItemRepository {
    fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>>
    fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>>
    fun updateTodoItem(item: TodoItemDomainEntity)
    fun updateDoneStatus(id: UUID, isDone: Boolean)
    fun deleteItem(id: UUID)
    fun swapItems(from: Int, to: Int)
}