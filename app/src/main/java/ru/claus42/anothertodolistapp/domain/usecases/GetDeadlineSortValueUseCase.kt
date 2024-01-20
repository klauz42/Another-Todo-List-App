package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.DeadlineSortValue
import javax.inject.Inject


class GetDeadlineSortValueUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    operator fun invoke(): Flow<DeadlineSortValue> {
        return searchOptionsPreferencesRepository.getDeadlineSortValue()
    }
}