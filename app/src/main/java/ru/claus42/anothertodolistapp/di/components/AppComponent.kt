package ru.claus42.anothertodolistapp.di.components

import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryBindModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryModule
import ru.claus42.anothertodolistapp.di.modules.ViewModelModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.presentation.viewmodels.AppViewModelsFactory
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity

//todo: make separated scopes

@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        TodoItemRepositoryModule::class,
        TodoItemRepositoryBindModule::class,
    ]
)
@AppScope
interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    fun viewModelsFactory(): AppViewModelsFactory
}