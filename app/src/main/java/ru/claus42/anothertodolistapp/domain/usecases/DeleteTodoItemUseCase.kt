package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import javax.inject.Inject


class DeleteTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(item: TodoItemDomainEntity) {
        repository.deleteItem(item)
    }
}