package ru.claus42.anothertodolistapp.domain.models.entities

import java.time.LocalDateTime
import java.util.UUID


enum class ItemPriority {
    LOW, BASIC, IMPORTANT
}


data class TodoItemDomainEntity(
    val id: UUID = UUID.randomUUID(),
    val description: String = "",
    val itemPriority: ItemPriority = ItemPriority.BASIC,
    val deadline: LocalDateTime = LocalDateTime.now(),
    val isDeadlineEnabled: Boolean = false,
    val done: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val changedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun equalsByContent(other: TodoItemDomainEntity?): Boolean {
        return if (other == null) {
            return false
        } else (
                id == other.id
                        && description == other.description
                        && itemPriority == other.itemPriority
                        && deadline == other.deadline
                        && isDeadlineEnabled == other.isDeadlineEnabled
                        && done == other.done
                )
    }
}