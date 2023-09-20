package ru.claus42.anothertodolistapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalEntity
import java.util.UUID


@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items")
    fun getTodoItems(): Flow<List<TodoItemLocalEntity>>

    @Query("SELECT * FROM todo_items WHERE id=(:id)")
    fun getTodoItem(id: UUID): Flow<TodoItemLocalEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<TodoItemLocalEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TodoItemLocalEntity)

    @Update
    suspend fun updateTodoItem(todoItem: TodoItemLocalEntity)

    @Insert
    suspend fun addTodoItem(todoItem: TodoItemLocalEntity)

    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItemLocalEntity)

    @Query("DELETE FROM todo_items")
    suspend fun clearTable()
}