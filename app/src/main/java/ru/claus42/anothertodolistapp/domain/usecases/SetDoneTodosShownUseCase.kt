package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.UserPreferencesRepository
import javax.inject.Inject


class SetDoneTodosShownUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(isDone: Boolean) {
        return userPreferencesRepository.setDoneTodosShown(isDone)
    }
}