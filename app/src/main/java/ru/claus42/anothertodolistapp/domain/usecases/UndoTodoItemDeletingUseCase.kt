package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import javax.inject.Inject


class UndoTodoItemDeletingUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke() {
        repository.undoDeletion()
    }
}