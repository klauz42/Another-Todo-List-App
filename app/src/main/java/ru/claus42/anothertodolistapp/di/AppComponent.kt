package ru.claus42.anothertodolistapp.di

import dagger.Component
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
}