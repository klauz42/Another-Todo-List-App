package ru.claus42.anothertodolistapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity


@Database(entities = [TodoItemLocalDataEntity::class], version = 2, exportSchema = true)
@TypeConverters(TodoItemTypeConverters::class)
abstract class TodoItemDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao
}