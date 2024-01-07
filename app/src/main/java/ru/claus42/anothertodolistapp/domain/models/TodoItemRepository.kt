package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID


interface TodoItemRepository {
    fun getTodoItems(): Flow<DataResult<List<TodoItemDomainEntity>>>
    fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>>
    suspend fun updateTodoItem(newItem: TodoItemDomainEntity)
    suspend fun updateDoneStatus(id: UUID, isDone: Boolean)
    suspend fun addItem(item: TodoItemDomainEntity)
    suspend fun deleteItem(item: TodoItemDomainEntity)
    suspend fun moveItem(fromId: UUID, toId: UUID)
    suspend fun undoDeletion()
    suspend fun clearRepository()
}