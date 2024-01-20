package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import javax.inject.Inject


class SetDeadlineSortValueUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(sortValue: DeadlineSortValue) {
        return searchOptionsPreferencesRepository.setDeadlineSortValue(sortValue)
    }
}