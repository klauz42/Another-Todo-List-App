package ru.claus42.anothertodolistapp.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.claus42.anothertodolistapp.di.scopes.AppScope

@Module
class AppModule(private val application: Application) {
    @Provides
    @AppScope
    fun provideApplicationContext(): Context = application.applicationContext
}