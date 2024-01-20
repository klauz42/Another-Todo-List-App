package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.ImportanceSortValue
import javax.inject.Inject


class SetImportanceSortUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(sortValue: ImportanceSortValue) {
        return searchOptionsPreferencesRepository.setImportanceSortValue(sortValue)
    }
}