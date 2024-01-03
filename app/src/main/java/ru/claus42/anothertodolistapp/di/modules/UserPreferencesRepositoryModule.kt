package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.repositories.UserPreferencesRepositoryImpl
import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository


@Module
interface UserPreferencesRepositoryModule {
    @Suppress("FunctionName")
    @Binds
    fun bindUserPreferencesImpl_to_UserPreferences(
        firebaseSessionManager: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}