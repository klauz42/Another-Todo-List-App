package ru.claus42.anothertodolistapp.data.remote

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import ru.claus42.anothertodolistapp.domain.models.DataResult
import java.util.UUID

interface NetworkServiceApi {
    fun getTodoItems(): Flow<DataResult<List<TodoItemRemoteDataEntity>>>
    fun getTodoItem(id: UUID): Flow<DataResult<TodoItemRemoteDataEntity>>
    suspend fun insertAll(items: List<TodoItemRemoteDataEntity>)
    suspend fun updateTodoItem(item: TodoItemRemoteDataEntity)
    suspend fun addTodoItem(item: TodoItemRemoteDataEntity)
    suspend fun updateAllOutdated(items: List<TodoItemRemoteDataEntity>)
    suspend fun deleteTodoItem(item: TodoItemRemoteDataEntity)
}