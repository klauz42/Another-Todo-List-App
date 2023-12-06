package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.repository.TodoItemRepositoryImpl
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository


@Module
interface TodoItemRepositoryBindModule {
    @Suppress("FunctionName")
    @Binds
    fun bindTodoItemRepositoryImpl_to_TodoItemRepository(
        todoItemRepositoryImpl: TodoItemRepositoryImpl
    ): TodoItemRepository
}