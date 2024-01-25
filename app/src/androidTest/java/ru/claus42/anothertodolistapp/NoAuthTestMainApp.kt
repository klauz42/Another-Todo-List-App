package ru.claus42.anothertodolistapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import ru.claus42.anothertodolistapp.di.components.AppComponent
import ru.claus42.anothertodolistapp.di.components.DaggerNoAuthTestAppComponent
import ru.claus42.anothertodolistapp.di.modules.AppModule

val Context.appComponent: AppComponent
    get() = when (this) {
        is NoAuthTestMainApp -> appComponent
        else -> this.applicationContext.appComponent
    }

class NoAuthTestMainApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerNoAuthTestAppComponent.factory().create(AppModule(this))
    }
}