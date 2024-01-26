package ru.claus42.anothertodolistapp.domain.models

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun isDoneTodosShown(): Flow<Boolean>
    suspend fun setDoneTodosShown(isDone: Boolean)
}