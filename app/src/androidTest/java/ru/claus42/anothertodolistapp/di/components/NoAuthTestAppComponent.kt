package ru.claus42.anothertodolistapp.di.components

import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.di.modules.DataStoreModule
import ru.claus42.anothertodolistapp.di.modules.NoAuthTestNetworkServiceApiModule
import ru.claus42.anothertodolistapp.di.modules.NoAuthTestSessionManagerModule
import ru.claus42.anothertodolistapp.di.modules.SearchOptionsPreferencesRepositoryModule
import ru.claus42.anothertodolistapp.di.modules.TestDatabaseModule
import ru.claus42.anothertodolistapp.di.modules.TodoItemRepositoryBindModule
import ru.claus42.anothertodolistapp.di.modules.UserPreferencesRepositoryModule
import ru.claus42.anothertodolistapp.di.scopes.AppScope


@AppScope
@Component(
    modules = [
        NoAuthTestSessionManagerModule::class,
        NoAuthTestNetworkServiceApiModule::class,
        TestDatabaseModule::class,
        AppModule::class,
        TodoItemRepositoryBindModule::class,
        DataStoreModule::class,
        UserPreferencesRepositoryModule::class,
        SearchOptionsPreferencesRepositoryModule::class,
    ]
)
interface NoAuthTestAppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): NoAuthTestAppComponent
    }
}