package ru.claus42.anothertodolistapp.data.remote.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.data.remote.mappers.ORDER_INDEX_KEY
import ru.claus42.anothertodolistapp.data.remote.mappers.UPDATED_AT_KEY
import ru.claus42.anothertodolistapp.data.remote.mappers.toMap
import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import ru.claus42.anothertodolistapp.di.scopes.AppScope
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.domain.models.DataResult
import java.util.UUID
import javax.inject.Inject


@AppScope
class FirebaseFirestoreServiceApi @Inject constructor(
    private val sessionManager: SessionManager
) : NetworkServiceApi {

    private val userId get() = sessionManager.getUserid() ?: ""
    private val db get() = Firebase.firestore

    private val tasksCollection
        get() =
            if (userId.isNotEmpty()) {
                db.collection(USERS_COLLECTION).document(userId).collection(TASKS_COLLECTION)
            } else {
                null
            }

    override fun getTodoItems(): Flow<DataResult<List<TodoItemRemoteDataEntity>>> = callbackFlow {
        val listenerRegistration = tasksCollection?.addSnapshotListener { tasks, error ->
            if (error != null) {
                trySend(DataResult.Error(error))
                return@addSnapshotListener
            }
            if (tasks != null) {
                val remoteEntities = tasks.documents.map { task ->
                    val remoteEntity = task.toObject(TodoItemRemoteDataEntity::class.java)
                    remoteEntity!!.copy(
                        taskId = task.id,
                        lastUpdatedBy = userId
                    )
                }
                trySend(DataResult.Success(remoteEntities))
            }
        }

        awaitClose {
            listenerRegistration?.remove()
        }
    }

    override fun getTodoItem(id: UUID): Flow<DataResult<TodoItemRemoteDataEntity>> = callbackFlow {
        val taskId = id.toString()

        val taskDocument = tasksCollection?.document(taskId)

        val listenerRegistration = taskDocument?.addSnapshotListener { task, error ->
            if (error != null) {
                trySend(DataResult.Error(error))
                return@addSnapshotListener
            }
            if (task != null) {
                val remoteEntityTemp = task.toObject(TodoItemRemoteDataEntity::class.java)
                val remoteEntity = remoteEntityTemp!!.copy(
                    taskId = task.id,
                    lastUpdatedBy = userId
                )
                trySend(DataResult.Success(remoteEntity))
            }
        }

        awaitClose {
            listenerRegistration?.remove()
        }
    }

    override fun insertAll(items: List<TodoItemRemoteDataEntity>) {
        for (item in items) {
            val id = item.taskId
            val task = item.toMap(userId)
            tasksCollection?.let {
                it.document(id).set(task)
                    .addOnFailureListener { e ->
                        //todo: add error handling, returning exception
                    }
            }
        }
    }

    override fun updateTodoItem(item: TodoItemRemoteDataEntity) {
        val id = item.taskId
        val task = item.toMap(userId)

        tasksCollection?.let {
            it.document(id).update(task)
                .addOnFailureListener {
                    //todo: add error handling, returning exception
                }
        }
    }

    override fun addTodoItem(item: TodoItemRemoteDataEntity) {
        val id = item.taskId
        val task = item.toMap(userId)

        tasksCollection?.let {
            it.document(id).set(task)
                .addOnFailureListener {
                    //todo: add error handling, returning exception
                }
        }
    }

    override fun updateAllOutdated(items: List<TodoItemRemoteDataEntity>) {
        for (item in items) {
            val id = item.taskId
            val task = item.toMap(userId)

            db.runTransaction { transaction ->
                tasksCollection?.let {
                    val docRef = it.document(id)
                    val snapshot = transaction.get(docRef)
                    val remoteUpdatedAt = snapshot.get(UPDATED_AT_KEY) as Long
                    val remoteOrderIndex = snapshot.get(ORDER_INDEX_KEY) as Long

                    val localUpdateAt = item.updatedAt

                    if (localUpdateAt > remoteUpdatedAt || remoteOrderIndex == -1L) {
                        transaction.update(docRef, task)
                    }
                }

            }.addOnFailureListener { e ->
                //todo: add error handling, returning exception
            }
        }
    }

    override fun deleteTodoItem(item: TodoItemRemoteDataEntity) {
        val id = item.taskId

        tasksCollection?.let {
            it.document(id).delete()
                .addOnFailureListener {
                    //todo: add error handling, returning exception
                }
        }
    }


    private companion object {
        private const val USERS_COLLECTION = "users"
        private const val TASKS_COLLECTION = "tasks"
    }
}