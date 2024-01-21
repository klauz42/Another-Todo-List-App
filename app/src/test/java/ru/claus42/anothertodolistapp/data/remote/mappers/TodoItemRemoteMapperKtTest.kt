package ru.claus42.anothertodolistapp.data.remote.mappers

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.claus42.anothertodolistapp.data.remote.models.TodoItemRemoteDataEntity
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

class TodoItemRemoteMapperKtTest {

    @Test
    fun toMap() {
        val userId = "testUserId"
        val taskId = "f0c72704-0f61-4e0a-83d3-30366c2221a9"
        val description = "Hello darkness\nMy old friend"
        val priority = 0L
        val deadline = 1706391277L
        val isDeadlineEnabled = true
        val done = false
        val createdAt = 1705786477L
        val updatedAt = 1705786477L
        val orderIndex = 4L

        val map = hashMapOf(
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

        val remote = TodoItemRemoteDataEntity(
            taskId = taskId,
            description = description,
            priority = priority,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastUpdatedBy = userId,
            orderIndex = orderIndex,
        )

        assertEquals(map, remote.toMap(userId))
    }

    @Test
    fun toRemoteDataModel() {
        val userId = "testUserId"
        val taskId = "f0c72704-0f61-4e0a-83d3-30366c2221a9"
        val description = "Hello darkness\nMy old friend"
        val priority = 1L
        val deadline = 1706391277L
        val isDeadlineEnabled = true
        val done = false
        val createdAt = 1705786477L
        val updatedAt = 1705786477L
        val orderIndex = 4L

        val domain = TodoItemDomainEntity(
            id = UUID.fromString(taskId),
            description = description,
            itemPriority = ItemPriority.BASIC,
            deadline = Instant.ofEpochSecond(deadline).atZone(ZoneId.systemDefault()),
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = Instant.ofEpochSecond(createdAt).atZone(ZoneId.systemDefault()),
            changedAt = Instant.ofEpochSecond(updatedAt).atZone(ZoneId.systemDefault()),
        )

        val remote = TodoItemRemoteDataEntity(
            taskId = taskId,
            description = description,
            priority = priority,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            updatedAt = updatedAt,
            lastUpdatedBy = userId,
            orderIndex = orderIndex,
        )

        assertEquals(
            remote,
            domain.toRemoteDataModel(orderIndex = orderIndex, lastUpdatedBy = userId)
        )
    }
}