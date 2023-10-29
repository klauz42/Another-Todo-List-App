package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.data.repository.TodoItemRepositoryImpl
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository

@Module
class TodoItemRepositoryModule {
    @AppScope
    @Provides
    fun providesTodoItemRepositoryImpl(): TodoItemRepositoryImpl {
        return TodoItemRepositoryImpl()
    }
}

@Module
interface TodoItemRepositoryBindModule {
    @AppScope
    @Suppress("FunctionName")
    @Binds
    fun bindTodoItemRepositoryImpl_to_TodoItemRepository(
        todoItemRepositoryImpl: TodoItemRepositoryImpl
    ): TodoItemRepository
}