package ru.claus42.anothertodolistapp.di.modules

import dagger.Module
import dagger.Provides
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager

@Module
class NoAuthTestSessionManagerModule {
    @AppScope
    @Provides
    fun provideTestSessionManager(): SessionManager {
        return mock<SessionManager> {
            on { isUserLoggedIn() } doReturn true
            on { getUserId() } doReturn "TestUserId"
            on { getUserEmail() } doReturn "test@test.com"
        }
    }
}