package ru.claus42.anothertodolistapp.data.remote.models

data class TodoItemRemoteDataEntity(
    val taskId: String = "",
    val description: String = "",
    val priority: Long = 0,
    val deadline: Long = 0,
    val isDeadlineEnabled: Boolean = false,
    val done: Boolean = false,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val lastUpdatedBy: String = "",
    val orderIndex: Long = -1,
)