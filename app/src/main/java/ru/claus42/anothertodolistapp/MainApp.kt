package ru.claus42.anothertodolistapp

import android.app.Application
import android.content.Context
import ru.claus42.anothertodolistapp.di.components.AppComponent
import ru.claus42.anothertodolistapp.di.components.DaggerAppComponent
import ru.claus42.anothertodolistapp.di.modules.AppModule

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> this.applicationContext.appComponent
    }

class MainApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(AppModule(this))
    }
}