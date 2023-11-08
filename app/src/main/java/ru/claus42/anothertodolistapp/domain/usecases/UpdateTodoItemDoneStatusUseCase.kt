package ru.claus42.anothertodolistapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import java.util.UUID
import javax.inject.Inject

class UpdateTodoItemDoneStatusUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    operator fun invoke(id: UUID, isDone: Boolean): Flow<DataResult<Nothing>> {
        return repository.updateDoneStatus(id, isDone)
    }
}