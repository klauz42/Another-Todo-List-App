package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import javax.inject.Inject


class SetDateSortValueUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(sortValue: DateSortValue) {
        return searchOptionsPreferencesRepository.setDateSortValue(sortValue)
    }
}