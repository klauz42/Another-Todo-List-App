package ru.claus42.anothertodolistapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import java.util.UUID


@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items")
    fun getTodoItems(): Flow<List<TodoItemLocalDataEntity>>

    @Query("SELECT * FROM todo_items WHERE id=(:id)")
    fun getTodoItem(id: UUID): Flow<TodoItemLocalDataEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<TodoItemLocalDataEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TodoItemLocalDataEntity)

    @Update
    suspend fun updateTodoItem(todoItem: TodoItemLocalDataEntity)

    @Insert
    suspend fun addTodoItem(todoItem: TodoItemLocalDataEntity)

    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItemLocalDataEntity)

    @Query("DELETE FROM todo_items")
    suspend fun clearTable()
}