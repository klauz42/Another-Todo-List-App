package ru.claus42.anothertodolistapp.di.components

import dagger.Component
import ru.claus42.anothertodolistapp.MainApp
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.di.modules.DataStoreModule
import ru.claus42.anothertodolistapp.di.modules.DatabaseModule
import ru.claus42.anothertodolistapp.di.modules.NetworkServiceApiModule
import ru.claus42.anothertodolistapp.di.modules.SearchOptionsPreferencesRepositoryModule
import ru.claus42.anothertodolistapp.di.modules.SessionManagerModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryBindModule
import ru.claus42.anothertodolistapp.di.modules.UserPreferencesRepositoryModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository


@AppScope
@Component(
    modules = [
        AppModule::class,
        TodoItemRepositoryBindModule::class,
        DatabaseModule::class,
        SessionManagerModule::class,
        DataStoreModule::class,
        UserPreferencesRepositoryModule::class,
        SearchOptionsPreferencesRepositoryModule::class,
        NetworkServiceApiModule::class
    ]
)
interface AppComponent {
    fun inject(application: MainApp)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    fun getTodoItemRepository(): TodoItemRepository
    fun getSessionManager(): SessionManager
    fun getUserPreferencesRepository(): UserPreferencesRepository
    fun getSortOptionsPreferencesRepository(): SearchOptionsPreferencesRepository
    fun getNetworkServiceApi(): NetworkServiceApi
}