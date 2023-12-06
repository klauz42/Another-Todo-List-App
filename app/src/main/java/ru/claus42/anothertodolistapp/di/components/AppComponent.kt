package ru.claus42.anothertodolistapp.di.components

import dagger.Component
import ru.claus42.anothertodolistapp.MainApp
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryBindModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository


@AppScope
@Component(
    modules = [
        AppModule::class,
        TodoItemRepositoryBindModule::class,
    ]
)
interface AppComponent {
    fun inject(application: MainApp)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    fun getTodoItemRepository(): TodoItemRepository
}