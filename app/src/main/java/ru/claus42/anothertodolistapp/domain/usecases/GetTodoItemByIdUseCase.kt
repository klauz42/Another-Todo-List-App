package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID
import javax.inject.Inject

class EditTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(id: UUID): Flow<DataResult<TodoItemDomainEntity>> {
        return repository.getTodoItem(id)
    }
}