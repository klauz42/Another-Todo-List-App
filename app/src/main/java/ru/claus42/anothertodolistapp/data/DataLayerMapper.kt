package ru.claus42.anothertodolistapp.data

import ru.claus42.anothertodolistapp.data.local.models.entities.LocalDataItemPriority
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID


private fun Long.toLocalDataItePriority(): LocalDataItemPriority {
    return when (this) {
        0L -> LocalDataItemPriority.LOW
        1L -> LocalDataItemPriority.BASIC
        2L -> LocalDataItemPriority.IMPORTANT
        else -> throw IllegalArgumentException("priority cannot be equal to $this")
    }
}

private fun LocalDataItemPriority.toLong(): Long {
    return when (this) {
        LocalDataItemPriority.LOW -> 0L
        LocalDataItemPriority.BASIC -> 1L
        LocalDataItemPriority.IMPORTANT -> 2L
    }
}

private fun Long.toZonedDateTime(): ZonedDateTime =
    Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault())

private fun ZonedDateTime.toLong(): Long = this.toEpochSecond()

fun TodoItemRemoteDataEntity.toTodoItemLocalDataEntity(): TodoItemLocalDataEntity {
    return TodoItemLocalDataEntity(
        id = UUID.fromString(taskId),
        description = description,
        priority = priority.toLocalDataItePriority(),
        deadline = deadline.toZonedDateTime(),
        isDeadlineEnabled = isDeadlineEnabled,
        done = done,
        createdAt = createdAt.toZonedDateTime(),
        changedAt = updatedAt.toZonedDateTime(),
        orderIndex = orderIndex
    )
}

fun TodoItemLocalDataEntity.toTodoItemRemoteEntity(): TodoItemRemoteDataEntity {
    return TodoItemRemoteDataEntity(
        taskId = id.toString(),
        description = description,
        priority = priority.toLong(),
        deadline = deadline.toLong(),
        isDeadlineEnabled = isDeadlineEnabled,
        done = done,
        createdAt = createdAt.toLong(),
        updatedAt = changedAt.toLong(),
        orderIndex = orderIndex
    )
}