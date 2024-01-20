package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import javax.inject.Inject


class GetImportanceSortValueUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    operator fun invoke(): Flow<ImportanceSortValue> {
        return searchOptionsPreferencesRepository.getImportanceSortValue()
    }
}