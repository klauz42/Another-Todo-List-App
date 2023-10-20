package ru.claus42.anothertodolistapp.di.components

import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryBindModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryModule
import ru.claus42.anothertodolistapp.di.modules.ViewModelModule
import ru.claus42.anothertodolistapp.presentation.viewmodels.AppViewModelsFactory
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity
import ru.claus42.anothertodolistapp.presentation.views.fragments.TodoItemListFragment
import javax.inject.Singleton

//todo: create separated

@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        TodoItemRepositoryModule::class,
        TodoItemRepositoryBindModule::class,
    ]
)
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: TodoItemListFragment)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    fun viewModelsFactory(): AppViewModelsFactory
}