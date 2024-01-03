package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import javax.inject.Inject


class UpdateTodoItemDoneStatusUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(item: TodoItemDomainEntity, isDone: Boolean) {
        repository.updateDoneStatus(item, isDone)
    }
}