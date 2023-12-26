package ru.claus42.anothertodolistapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.data.local.models.mappers.toLocalDataModel
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.util.UUID
import javax.inject.Inject


@AppScope
class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao
) : TodoItemRepository {

    private var lastDeleted: TodoItemLocalDataEntity? = null
    private var lastDeletedPosition: Int? = null

    override fun getTodoItems(): Flow<DataResult<List<TodoItemDomainEntity>>> =
        flow {
            emit(DataResult.loading())
            try {
                todoItemDao.getTodoItems().collect { cachedTodoItems ->
                    emit(DataResult.Success(cachedTodoItems.map { it.toDomainModel() }))
                }
            } catch (e: Exception) {
                emit(DataResult.Error(e))
            }
        }

    override fun getTodoItem(id: UUID): Flow<DataResult<TodoItemDomainEntity>> = flow {
        emit(DataResult.loading())

        try {
            todoItemDao.getTodoItem(id).collect { item ->
                emit(DataResult.Success(item.toDomainModel()))
            }
        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }

    override suspend fun updateTodoItem(newItem: TodoItemDomainEntity) {
        todoItemDao.updateTodoItem(newItem.toLocalDataModel())
    }

    override suspend fun updateDoneStatus(id: UUID, isDone: Boolean) {
        todoItemDao.updateDoneStatus(id, isDone)
    }

    override suspend fun deleteItem(item: TodoItemDomainEntity) {
        lastDeleted = item.toLocalDataModel()
        lastDeletedPosition = todoItemDao.deleteTodoItem(item.toLocalDataModel())
    }

    override suspend fun moveItem(fromId: UUID, toId: UUID) {
        todoItemDao.moveItem(fromId, toId)
    }


    override suspend fun addItem(item: TodoItemDomainEntity) {
        todoItemDao.addTodoItem(0, item.toLocalDataModel())
    }

    override suspend fun undoDeletion() {
        lastDeleted?.let { item ->
            lastDeletedPosition?.let { pos ->
                todoItemDao.addTodoItem(pos, item)
            }
        }
    }
}

