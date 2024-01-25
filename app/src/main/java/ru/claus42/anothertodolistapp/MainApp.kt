package ru.claus42.anothertodolistapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import ru.claus42.anothertodolistapp.di.components.AppComponent
import ru.claus42.anothertodolistapp.di.components.DaggerAppComponent
import ru.claus42.anothertodolistapp.di.modules.AppModule
import ru.claus42.anothertodolistapp.utils.Constants.Notifications.NO_INTERNET_NOTIFICATION_CHANNEL_DESCRIPTION
import ru.claus42.anothertodolistapp.utils.Constants.Notifications.NO_INTERNET_NOTIFICATION_CHANNEL_ID
import ru.claus42.anothertodolistapp.utils.Constants.Notifications.NO_INTERNET_NOTIFICATION_CHANNEL_NAME

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

        setupNoInternetNotificationChannel()
    }

    //todo: add proposal dialog to enable notification
    private fun setupNoInternetNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NO_INTERNET_NOTIFICATION_CHANNEL_NAME
            val descriptionText = NO_INTERNET_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(
                NO_INTERNET_NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "MainApp"
    }
}