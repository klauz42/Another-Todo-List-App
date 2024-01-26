package ru.claus42.anothertodolistapp.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.claus42.anothertodolistapp.data.local.TodoItemDao
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.local.models.mappers.toDomainModel
import ru.claus42.anothertodolistapp.data.local.models.mappers.toLocalDataModel
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.data.remote.mappers.toRemoteDataModel
import ru.claus42.anothertodolistapp.data.toTodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.toTodoItemRemoteEntity
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.models.DataResult
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject


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
        val item = newItem.copy(changedAt = ZonedDateTime.now())
        todoItemDao.updateTodoItem(item.toLocalDataModel())

        try {
            networkServiceApi.updateTodoItem(item.toRemoteDataModel())
        } catch (e: Exception) {
            Log.e(TAG, "updateTodoItem: ${e.message}")
            throw e
        }
    }

    override suspend fun updateDoneStatus(id: UUID, isDone: Boolean) {
        val item = todoItemDao.getTodoItem(id).first().copy(
            done = isDone,
            changedAt = ZonedDateTime.now(),
        )
        todoItemDao.updateTodoItem(item)

        try {
            networkServiceApi.updateTodoItem(item.toTodoItemRemoteEntity())
        } catch (e: Exception) {
            Log.e(TAG, "updateDoneStatus: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteItem(item: TodoItemDomainEntity) {
        lastDeleted = item.toLocalDataModel()
        lastDeletedPosition = todoItemDao.deleteTodoItem(item.toLocalDataModel())

        try {
            networkServiceApi.deleteTodoItem(item.toRemoteDataModel())
            updateRemoteFromLocal()
        } catch (e: Exception) {
            Log.e(TAG, "deleteItem: ${e.message}")
            throw e
        }

    }

    override suspend fun moveItem(fromId: UUID, toId: UUID) {
        todoItemDao.moveItem(fromId, toId)

        try {
            updateRemoteFromLocal()
        } catch (e: Exception) {
            Log.e(TAG, "moveItem: ${e.message}")
            throw e
        }
    }


    override suspend fun addItem(item: TodoItemDomainEntity) {
        todoItemDao.addTodoItem(0, item.toLocalDataModel())
        try {
            networkServiceApi.addTodoItem(item.toRemoteDataModel())
            updateRemoteFromLocal()
        } catch (e: Exception) {
            Log.e(TAG, "addItem: ${e.message}")
            throw e
        }
    }

    override suspend fun undoDeletion() {
        lastDeleted?.let { item ->
            lastDeletedPosition?.let { pos ->
                todoItemDao.addTodoItem(pos, item)

                try {
                    networkServiceApi.addTodoItem(item.toTodoItemRemoteEntity())
                    updateRemoteFromLocal()
                } catch (e: Exception) {
                    Log.e(TAG, "undoDeletion: ${e.message}")
                    throw e
                }
            }
        }
    }

    override suspend fun clearRepository() {
        todoItemDao.clearTable()
    }

    private val MAX_TRIES = 5
    private var syncTries = 0

    override suspend fun syncLocalWithRemote() {
        try {
            when (val result = networkServiceApi.getTodoItems().first()) {
                is DataResult.Success -> {
                    Log.d(TAG, "Got to-dos from network api")

                    val remoteItems = result.data

                    remoteItems.forEach { remoteItem ->
                        val id = UUID.fromString(remoteItem.taskId)
                        val localItem: TodoItemLocalDataEntity

                        try {
                            localItem = todoItemDao.getTodoItem(id).first()
                            if (localItem == null)
                                throw NoSuchElementException("$id is not in local db")

                            val remoteChangedTime = Instant
                                .ofEpochSecond(remoteItem.updatedAt)
                                .atZone(ZoneId.systemDefault())

                            if (localItem.changedAt > remoteChangedTime) {
                                Log.d(TAG, "$id is newer in local db, updating remote")
                                networkServiceApi.updateTodoItem(localItem.toTodoItemRemoteEntity())
                            } else if (localItem.changedAt < remoteChangedTime) {
                                Log.d(TAG, "$id is newer in remote db, updating local")
                                todoItemDao.updateTodoItem(remoteItem.toTodoItemLocalDataEntity())
                            }
                        } catch (e: NoSuchElementException) {
                            Log.d(TAG, "${e.message}")
                            todoItemDao.addTodoItem(0, remoteItem.toTodoItemLocalDataEntity())
                        }
                    }
                }

                is DataResult.Error -> {
                    Log.e(TAG, "syncLocalWithRemote: ${result.error.message}")
                    throw result.error
                }

                DataResult.Loading -> {
                    Log.d(
                        TAG,
                        "Got loading state during getting to-dos from network api ($syncTries)"
                    )
                    if (syncTries < MAX_TRIES) {
                        syncTries++
                        syncLocalWithRemote()
                    }
                }

                DataResult.OK -> {}
            }

            updateRemoteFromLocal()
        } catch (e: Exception) {
            Log.e(TAG, "syncLocalWithRemote: ${e.message}")
            throw e
        } finally {
            syncTries = 0
        }
    }

    private suspend fun updateRemoteFromLocal() {
        val items = todoItemDao.getTodoItems().first()
        networkServiceApi.updateAllOutdated(items.map { it.toTodoItemRemoteEntity() })
    }

    private companion object {
        const val TAG = "TodoItemRepositoryImpl"
    }
}

