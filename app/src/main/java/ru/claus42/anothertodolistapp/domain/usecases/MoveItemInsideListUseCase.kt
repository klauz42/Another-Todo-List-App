package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import javax.inject.Inject

class MoveItemInsideListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(fromPosition: Int, toPosition: Int) {
        repository.moveItem(fromPosition, toPosition)
    }
}