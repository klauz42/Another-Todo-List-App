package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import javax.inject.Inject


class UpdateTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(newItem: TodoItemDomainEntity) {
        repository.updateTodoItem(newItem)
    }
}