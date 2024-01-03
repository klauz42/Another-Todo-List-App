package ru.claus42.anothertodolistapp.data.remote

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import ru.claus42.anothertodolistapp.domain.models.DataResult
import java.util.UUID

interface NetworkServiceApi {
    fun getTodoItems(): Flow<DataResult<List<TodoItemRemoteDataEntity>>>
    fun getTodoItem(id: UUID): Flow<DataResult<TodoItemRemoteDataEntity>>
    fun insertAll(items: List<TodoItemRemoteDataEntity>)
    fun updateTodoItem(item: TodoItemRemoteDataEntity)
    fun addTodoItem(item: TodoItemRemoteDataEntity)
    fun updateAllOutdated(items: List<TodoItemRemoteDataEntity>)
    fun deleteTodoItem(item: TodoItemRemoteDataEntity)
}