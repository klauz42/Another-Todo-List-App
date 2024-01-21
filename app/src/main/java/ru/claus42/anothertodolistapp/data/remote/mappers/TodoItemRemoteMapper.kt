package ru.claus42.anothertodolistapp.data.remote.mappers

import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity


const val LAST_UPDATED_BY_KEY = "lastUpdatedBy"
const val TASK_ID_KEY = "taskId"
const val DESCRIPTION_KEY = "description"
const val PRIORITY_KEY = "priority"
const val DEADLINE_KEY = "deadline"
const val IS_DEADLINE_ENABLED_KEY = "isDeadlineEnabled"
const val DONE_KEY = "done"
const val CREATED_AT_KEY = "createdAt"
const val UPDATED_AT_KEY = "updatedAt"
const val ORDER_INDEX_KEY = "orderIndex"

fun TodoItemRemoteDataEntity.toMap(userId: String): Map<String, Any> {
    return hashMapOf(
        LAST_UPDATED_BY_KEY to userId,
        TASK_ID_KEY to taskId,
        DESCRIPTION_KEY to description,
        PRIORITY_KEY to priority,
        DEADLINE_KEY to deadline,
        IS_DEADLINE_ENABLED_KEY to isDeadlineEnabled,
        DONE_KEY to done,
        CREATED_AT_KEY to createdAt,
        UPDATED_AT_KEY to updatedAt,
        ORDER_INDEX_KEY to orderIndex,
    )
}

private fun ItemPriority.toLong(): Long {
    return when (this) {
        ItemPriority.LOW -> 0
        ItemPriority.BASIC -> 1
        ItemPriority.IMPORTANT -> 2
    }
}

fun TodoItemDomainEntity.toRemoteDataModel(
    orderIndex: Long = 0,
    lastUpdatedBy: String = "",
): TodoItemRemoteDataEntity {
    return TodoItemRemoteDataEntity(
        taskId = id.toString(),
        description = description,
        priority = itemPriority.toLong(),
        deadline = deadline.toEpochSecond(),
        isDeadlineEnabled = isDeadlineEnabled,
        done = done,
        createdAt = createdAt.toEpochSecond(),
        updatedAt = changedAt.toEpochSecond(),
        orderIndex = orderIndex,
        lastUpdatedBy = lastUpdatedBy,
    )
}
