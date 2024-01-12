package ru.claus42.anothertodolistapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.TodoItemDatabase
import ru.claus42.anothertodolistapp.data.local.MIGRATION_1_2
import ru.claus42.anothertodolistapp.di.scopes.AppScope


const val DATABASE_NAME = "todo-item-database"

@Module
object DatabaseModule {
    @Provides
    @AppScope
    fun provideTodoItemDatabase(context: Context): TodoItemDatabase {
        return Room.databaseBuilder(
            context,
            TodoItemDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Provides
    @AppScope
    fun provideTodoItemDao(database: TodoItemDatabase): TodoItemDao {
        return database.todoItemDao()
    }
}