package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import java.util.UUID
import javax.inject.Inject


class UpdateTodoItemDoneStatusUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(id: UUID, isDone: Boolean) {
        repository.updateDoneStatus(id, isDone)
    }
}