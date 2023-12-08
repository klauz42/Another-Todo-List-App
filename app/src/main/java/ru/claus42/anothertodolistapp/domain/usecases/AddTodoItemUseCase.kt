package ru.claus42.anothertodolistapp.domain.usecases

import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import javax.inject.Inject

class AddTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(item: TodoItemDomainEntity) {
        repository.addItem(item)
    }
}