package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DateSortValue
import javax.inject.Inject


class GetDateSortValueUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    operator fun invoke(): Flow<DateSortValue> {
        return searchOptionsPreferencesRepository.getDateSortValue()
    }
}