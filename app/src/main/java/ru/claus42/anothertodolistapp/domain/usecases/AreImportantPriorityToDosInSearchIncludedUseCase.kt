package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import javax.inject.Inject


class AreImportantPriorityToDosInSearchIncludedUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return searchOptionsPreferencesRepository.areImportantPriorityTodosIncluded()
    }
}