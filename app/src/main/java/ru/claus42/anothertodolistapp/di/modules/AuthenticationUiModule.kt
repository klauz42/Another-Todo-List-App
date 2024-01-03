package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.auth.FirebaseAuthenticator
import ru.claus42.anothertodolistapp.domain.authentication.Authenticator

@Module
interface AuthenticationUiModule {
    @Suppress("FunctionName")
    @Binds
    fun bindFirebaseAuthenticator_to_Authenticator(
        firebaseAuthenticator: FirebaseAuthenticator
    ): Authenticator
}