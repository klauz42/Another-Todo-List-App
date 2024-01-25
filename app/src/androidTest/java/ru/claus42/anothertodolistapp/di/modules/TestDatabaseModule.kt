package ru.claus42.anothertodolistapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.TodoItemDatabase
import ru.claus42.anothertodolistapp.di.scopes.AppScope


@Module
object TestDatabaseModule {
    @Provides
    @AppScope
    fun provideTodoItemDatabase(context: Context): TodoItemDatabase {
        return Room.inMemoryDatabaseBuilder(context, TodoItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @AppScope
    fun provideTodoItemDao(database: TodoItemDatabase): TodoItemDao {
        return database.todoItemDao()
    }
}