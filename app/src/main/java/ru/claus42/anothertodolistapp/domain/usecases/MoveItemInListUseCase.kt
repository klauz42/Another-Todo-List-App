package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import java.util.UUID
import javax.inject.Inject


class MoveItemInListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(fromId: UUID, toId: UUID) {
        repository.moveItem(fromId, toId)
    }
}