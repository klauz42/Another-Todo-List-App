package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import java.util.UUID
import javax.inject.Inject

class DeleteTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(id: UUID) {
        repository.deleteItem(id)
    }
}