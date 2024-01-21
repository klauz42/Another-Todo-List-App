package ru.claus42.anothertodolistapp.data.local.models.mappers

import org.junit.Assert.*

import org.junit.Test
import ru.claus42.anothertodolistapp.data.local.models.entities.LocalDataItemPriority
import ru.claus42.anothertodolistapp.data.local.models.entities.TodoItemLocalDataEntity
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
import ru.claus42.anothertodolistapp.domain.models.entities.TodoItemDomainEntity
import java.time.ZonedDateTime
import java.util.UUID

class TodoItemLocalMapperKtTest {

    private val id = UUID.randomUUID()
    private val description = "aaa"
    private val localBasicPriority = LocalDataItemPriority.BASIC
    private val localLowPriority = LocalDataItemPriority.LOW
    private val localImportantPriority = LocalDataItemPriority.IMPORTANT
    private val deadline = ZonedDateTime.now()
    private val isDeadlineEnabled = false
    private val done = true
    private val createdAt = ZonedDateTime.now().plusDays(5)
    private val changedAt = ZonedDateTime.now().minusDays(4)
    private val orderIndex = 4L

    @Test
    fun toDomainModel() {
        val basicLocal = TodoItemLocalDataEntity(
            id = id,
            description = description,
            priority = localBasicPriority,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
            orderIndex = orderIndex
        )
        val basicDomain = basicLocal.toDomainModel()
        assertEquals(id, basicDomain.id)
        assertEquals(description, basicDomain.description)
        assertEquals(ItemPriority.BASIC, basicDomain.itemPriority)
        assertEquals(deadline, basicDomain.deadline)
        assertEquals(isDeadlineEnabled, basicDomain.isDeadlineEnabled)
        assertEquals(done, basicDomain.done)
        assertEquals(createdAt, basicDomain.createdAt)
        assertEquals(changedAt, basicDomain.changedAt)

        val lowLocal = TodoItemLocalDataEntity(
            id = id,
            description = description,
            priority = localLowPriority,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
            orderIndex = orderIndex
        )
        val lowDomain = lowLocal.toDomainModel()
        assertEquals(ItemPriority.LOW, lowDomain.itemPriority)

        val importantLocal = TodoItemLocalDataEntity(
            id = id,
            description = description,
            priority = localImportantPriority,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
            orderIndex = orderIndex
        )
        val importantDomain = importantLocal.toDomainModel()
        assertEquals(ItemPriority.IMPORTANT, importantDomain.itemPriority)
    }

    @Test
    fun toLocalDataModel() {
        val basicDomain = TodoItemDomainEntity(
            id = id,
            description = description,
            ItemPriority.BASIC,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
        )
        val basicLocal = basicDomain.toLocalDataModel()
        assertEquals(id, basicLocal.id)
        assertEquals(description, basicLocal.description)
        assertEquals(LocalDataItemPriority.BASIC, basicLocal.priority)
        assertEquals(deadline, basicLocal.deadline)
        assertEquals(isDeadlineEnabled, basicLocal.isDeadlineEnabled)
        assertEquals(done, basicLocal.done)
        assertEquals(createdAt, basicLocal.createdAt)
        assertEquals(changedAt, basicLocal.changedAt)

        val lowDomain = TodoItemDomainEntity(
            id = id,
            description = description,
            ItemPriority.LOW,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
        )
        val lowLocal = lowDomain.toLocalDataModel()
        assertEquals(LocalDataItemPriority.LOW, lowLocal.priority)

        val importantDomain = TodoItemDomainEntity(
            id = id,
            description = description,
            ItemPriority.IMPORTANT,
            deadline = deadline,
            isDeadlineEnabled = isDeadlineEnabled,
            done = done,
            createdAt = createdAt,
            changedAt = changedAt,
        )
        val importantLocal = importantDomain.toLocalDataModel()
        assertEquals(LocalDataItemPriority.IMPORTANT, importantLocal.priority)
    }
}