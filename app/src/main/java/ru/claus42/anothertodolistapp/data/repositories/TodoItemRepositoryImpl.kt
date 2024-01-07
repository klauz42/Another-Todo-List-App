package ru.claus42.anothertodolistapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.data.local.models.mappers.toLocalDataModel
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.data.remote.mappers.toRemoteDataModel
import ru.claus42.anothertodolistapp.data.toTodoItemRemoteEntity
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


//TODO: add throwing exception (add returning type to every method)
@AppScope
class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val networkServiceApi: NetworkServiceApi,
) : TodoItemRepository {

    private var lastDeleted: TodoItemLocalDataEntity? = null
    private var lastDeletedPosition: Long? = null

    override fun getTodoItems(): Flow<DataResult<List<TodoItemDomainEntity>>> = flow {
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
        val item = newItem.copy(changedAt = LocalDateTime.now())
        todoItemDao.updateTodoItem(item.toLocalDataModel())

        try {
            networkServiceApi.updateTodoItem(item.toRemoteDataModel())
        } catch (e: Exception) {
            //TODO: throw exception
        }
    }

    override suspend fun updateDoneStatus(id: UUID, isDone: Boolean) {
        val item = todoItemDao.getTodoItem(id).first().copy(
            done = isDone,
            changedAt = LocalDateTime.now(),
        )
        todoItemDao.updateTodoItem(item)

        try {
            networkServiceApi.updateTodoItem(item.toTodoItemRemoteEntity())
        } catch (e: Exception) {
            //TODO: throw exception
        }
    }

    override suspend fun deleteItem(item: TodoItemDomainEntity) {
        lastDeleted = item.toLocalDataModel()
        lastDeletedPosition = todoItemDao.deleteTodoItem(item.toLocalDataModel())

        networkServiceApi.deleteTodoItem(item.toRemoteDataModel())
        updateRemoteSource()
    }

    override suspend fun moveItem(fromId: UUID, toId: UUID) {
        todoItemDao.moveItem(fromId, toId)

        updateRemoteSource()
    }


    override suspend fun addItem(item: TodoItemDomainEntity) {
        todoItemDao.addTodoItem(0, item.toLocalDataModel())
        networkServiceApi.addTodoItem(item.toRemoteDataModel())

        updateRemoteSource()
    }

    override suspend fun undoDeletion() {
        lastDeleted?.let { item ->
            lastDeletedPosition?.let { pos ->
                todoItemDao.addTodoItem(pos, item)
                networkServiceApi.addTodoItem(item.toTodoItemRemoteEntity())

                updateRemoteSource()
            }
        }
    }

    override suspend fun clearRepository() {
        todoItemDao.clearTable()
    }

    private suspend fun updateRemoteSource() = DataResult.onWithoutData {
        val items = todoItemDao.getTodoItems().first()

        try {
            networkServiceApi.updateAllOutdated(items.map { it.toTodoItemRemoteEntity() })
        } catch (e: Exception) {
            TODO("Not yet implemented")
        }
    }
}

