package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.SearchOptionsPreferencesRepository
import ru.claus42.anothertodolistapp.domain.models.entities.SearchLayoutViewType
import javax.inject.Inject


class SetSearchLayoutViewTypeUseCase @Inject constructor(
    private val searchOptionsPreferencesRepository: SearchOptionsPreferencesRepository
) {
    suspend operator fun invoke(layoutViewType: SearchLayoutViewType) {
        return searchOptionsPreferencesRepository.setSearchLayoutViewType(layoutViewType)
    }
}