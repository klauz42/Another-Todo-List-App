package ru.claus42.anothertodolistapp

import ru.claus42.anothertodolistapp.di.components.DaggerNoAuthTestAppComponent
import ru.claus42.anothertodolistapp.di.modules.AppModule


class NoAuthTestMainApp : MainApp() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerNoAuthTestAppComponent.factory().create(AppModule(this))
    }
}