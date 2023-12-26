package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import javax.inject.Inject


class GetTodoItemListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(): Flow<DataResult<List<TodoItemDomainEntity>>> {
        return repository.getTodoItems()
    }
}