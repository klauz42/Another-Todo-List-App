package ru.claus42.anothertodolistapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import java.util.UUID


@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items ORDER BY order_index")
    fun getTodoItems(): Flow<List<TodoItemLocalDataEntity>>

    @Query("SELECT * FROM todo_items WHERE id=(:id)")
    fun getTodoItem(id: UUID): Flow<TodoItemLocalDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TodoItemLocalDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoItemLocalDataEntity)

    @Transaction
    suspend fun updateTodoItem(item: TodoItemLocalDataEntity) {
        val position = _getOrderIndexById(item.id)!!
        _update(item.copy(orderIndex = position))
    }

    @Transaction
    suspend fun addTodoItem(orderIndex: Long, item: TodoItemLocalDataEntity) {
        _incrementOrderIndicesAfterInclByOrderIndex(orderIndex)
        _add(item.copy(orderIndex = orderIndex))
    }

    @Transaction
    suspend fun moveItem(fromId: UUID, toId: UUID) {
        val fromOrderIndex = _getOrderIndexById(fromId)!!
        val toOrderIndex = _getOrderIndexById(toId)!!

        if (fromOrderIndex < toOrderIndex) {
            //moving down
            _decrementOrderIndicesRangeMovingItemDown(fromOrderIndex, toOrderIndex)
            _setOrderIndexById(fromId, toOrderIndex)
        } else if (fromOrderIndex > toOrderIndex) {
            //moving up
            _incrementOrderIndicesRangeMovingItemUp(fromOrderIndex, toOrderIndex)
            _setOrderIndexById(fromId, toOrderIndex)
        }
    }

    @Transaction
    suspend fun deleteTodoItem(item: TodoItemLocalDataEntity): Long {
        val orderIndex = _getOrderIndexById(item.id)!!
        _decrementOrderIndicesAfterExclById(item.id)
        _delete(item)

        return orderIndex
    }

    @Query("SELECT MAX(order_index) FROM todo_items")
    suspend fun getMaximumOrderIndex(): Long?

    @Query("DELETE FROM todo_items")
    suspend fun clearTable()


    @Insert
    suspend fun _add(item: TodoItemLocalDataEntity)

    @Delete
    suspend fun _delete(item: TodoItemLocalDataEntity)

    @Update
    suspend fun _update(item: TodoItemLocalDataEntity)

    @Query("UPDATE todo_items SET order_index = order_index + 1 WHERE order_index >= (:orderIndex)")
    suspend fun _incrementOrderIndicesAfterInclByOrderIndex(orderIndex: Long)

    @Query(
        "UPDATE todo_items SET order_index = order_index - 1 " +
                " WHERE order_index > (SELECT order_index FROM todo_items WHERE id = (:id))"
    )
    suspend fun _decrementOrderIndicesAfterExclById(id: UUID)

    @Query(
        "UPDATE todo_items SET order_index = order_index + 1 " +
                " WHERE order_index < (:fromOrderIndex) AND order_index >= (:toOrderIndex) "
    )
    suspend fun _incrementOrderIndicesRangeMovingItemUp(fromOrderIndex: Long, toOrderIndex: Long)

    @Query(
        "UPDATE todo_items SET order_index = order_index - 1 " +
                " WHERE order_index <= (:toOrderIndex) AND order_index > (:fromOrderIndex) "
    )
    suspend fun _decrementOrderIndicesRangeMovingItemDown(fromOrderIndex: Long, toOrderIndex: Long)

    @Query("UPDATE todo_items SET order_index = (:newOrderIndex) WHERE id = (:id)")
    suspend fun _setOrderIndexById(id: UUID, newOrderIndex: Long)

    @Query("SELECT order_index FROM todo_items WHERE id = (:id)")
    suspend fun _getOrderIndexById(id: UUID): Long?

}