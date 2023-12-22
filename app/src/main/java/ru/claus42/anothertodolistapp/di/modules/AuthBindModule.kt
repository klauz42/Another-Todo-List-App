package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.auth.FirebaseAuthenticator
import ru.claus42.anothertodolistapp.data.auth.FirebaseSessionManager
import ru.claus42.anothertodolistapp.domain.authentication.Authenticator
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager

@Module
interface AuthBindModule {
    @Suppress("FunctionName")
    @Binds
    fun bindFirebaseSessionManager_to_SessionManager(
        firebaseSessionManager: FirebaseSessionManager
    ): SessionManager

    @Suppress("FunctionName")
    @Binds
    fun bindFirebaseAuthenticator_to_Authenticator(
        firebaseAuthenticator: FirebaseAuthenticator
    ): Authenticator

}