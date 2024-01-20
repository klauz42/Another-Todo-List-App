package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import javax.inject.Inject


class SetOnlyWithDeadlineToDosInSearchIncludedUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(included: Boolean) {
        return searchOptionsPreferencesRepository.setOnlyWithDeadlineIncluded(included)
    }
}