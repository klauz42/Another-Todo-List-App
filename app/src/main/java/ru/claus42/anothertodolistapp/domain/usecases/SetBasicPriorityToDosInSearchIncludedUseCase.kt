package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import javax.inject.Inject


class SetBasicPriorityToDosInSearchIncludedUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(basicIncluded: Boolean) {
        return searchOptionsPreferencesRepository.setBasicPriorityTodosIncluded(basicIncluded)
    }
}