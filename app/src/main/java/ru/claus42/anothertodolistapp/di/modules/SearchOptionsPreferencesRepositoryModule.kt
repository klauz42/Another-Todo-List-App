package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.repositories.SearchOptionsPreferencesRepositoryImpl
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository


@Module
interface SearchOptionsPreferencesRepositoryModule {
    @Suppress("FunctionName")
    @Binds
    fun bindSearchOptionsPreferencesImpl_to_SearchOptionsPreferences(
        sortOptionsPreferencesRepository: SearchOptionsPreferencesRepositoryImpl
    ): SearchOptionsPreferencesRepository
}