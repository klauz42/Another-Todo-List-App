package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import javax.inject.Inject

class SwapItemsInsideListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(fromPosition: Int, toPosition: Int) {
        repository.swapItems(fromPosition, toPosition)
    }
}