package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.auth.FirebaseSessionManager
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager

@Module
interface SessionManagerModule {
    @Suppress("FunctionName")
    @Binds
    fun bindFirebaseSessionManager_to_SessionManager(
        firebaseSessionManager: FirebaseSessionManager
    ): SessionManager
}