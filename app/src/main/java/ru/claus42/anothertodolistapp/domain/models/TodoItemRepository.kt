package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID

interface TodoItemRepository {
    fun getTodoItemList(): Flow<DataResult<List<TodoItemDomainEntity>>>
    fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>>
    fun updateTodoItem(newItem: TodoItemDomainEntity)
    fun updateDoneStatus(id: UUID, isDone: Boolean)
    fun addItem(item: TodoItemDomainEntity)
    fun deleteItem(id: UUID)
    fun moveItem(fromId: UUID, toId: UUID)
    fun undoDeletion()
}