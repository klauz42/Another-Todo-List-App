package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortType
import javax.inject.Inject


class GetDateSortTypeUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    operator fun invoke(): Flow<DateSortType> {
        return searchOptionsPreferencesRepository.getDateSortType()
    }
}