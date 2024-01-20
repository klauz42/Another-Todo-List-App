package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import javax.inject.Inject


class SetDateSortTypeUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(sortType: DateSortType) {
        return searchOptionsPreferencesRepository.setDateSortType(sortType)
    }
}